using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Chatterly_Backend.Data.DTOs;
using Chatterly_Backend.Data.Models;
using Chatterly_Backend.Repositories;

namespace Chatterly_Backend.Controllers;

[ApiController]
[Route("[controller]")]
public class UsersController(IUsersRepository usersRepository, ILogger<UsersController> logger) : ControllerBase
{
    [HttpGet]
    [Route("{userId}")]
    [Authorize()]
    public async Task<ActionResult<UserDTO>> GetUser(string userId)
    {
        logger.LogDebug("UsersController.GetUser: Getting user with id: {userId}", userId);
        UserDTO? user = await usersRepository.GetUser(userId);
        return user != null ? Ok(user) : NotFound("User not found");
    }

    [HttpGet]
    [Route("")]
    //[Authorize()]
    public async Task<ActionResult<IEnumerable<UserDTO>>> SearchUsers(string query, DateTime? lastDate, string? lastId)
    {
        logger.LogDebug("UsersController.SearchUsers: Searching for users with query: {query}", query);
        return Ok(await usersRepository.SearchUsers(query, lastDate, lastId));
    }

    [HttpPut]
    [Route("{userId}")]
    [Authorize()]
    public async Task<ActionResult> UpdateUser(string userId, [FromBody] UserDTO user)
    {
        logger.LogDebug("UsersController.UpdateUser: Updating user {@User}", user);
        await usersRepository.UpdateUser(user);
        return Ok();
    }

    [HttpGet]
    [Route("Username/{userName}/Available")]
    public async Task<ActionResult<bool>> IsUserNameAvailable(string userName)
    {
        logger.LogDebug("UsersController.IsUserNameAvailable: Checking if username {userName} is available", userName);
        return Ok(await usersRepository.IsUserNameAvailable(userName));
    }
}