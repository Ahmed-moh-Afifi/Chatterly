using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Chatterly_Backend.Data;
using Chatterly_Backend.Data.Models;
using Chatterly_Backend.Data.DTOs;

namespace Chatterly_Backend.Repositories;

public class UsersRepository(ApplicationDbContext dbContext, UserManager<User> userManager, ILogger<UsersRepository> logger) : IUsersRepository
{
    public async Task<IEnumerable<UserDTO>> SearchUsers(string query, DateTime? lastDate, string? lastId)
    {
        logger.LogDebug("UsersRepository.SearchUsers: Searching for users with query: {query}", query);
        try
        {
            List<UserDTO> users;
            if (lastDate == null && lastId == null)
            {
                logger.LogDebug("UsersRepository.SearchUsers: Getting first 10 users with query: {query}", query);
                users = await dbContext.Users
                    .Where(user =>
                        user.UserName!.Contains(query) ||
                        user.FirstName.Contains(query) ||
                        user.LastName.Contains(query) ||
                        query.Contains(user.UserName) ||
                        query.Contains(user.FirstName) ||
                        query.Contains(user.LastName))
                    .OrderBy(u => u.JoinDate)
                    .ThenBy(u => u.Id)
                    .Select(u => u.ToDTO())
                    .Take(10)
                    .ToListAsync();
            }
            else
            {
                logger.LogDebug("UsersRepository.SearchUsers: Getting users with query: {query} after date {lastDate} and id {lastId}", query, lastDate, lastId);
                users = await dbContext.Users
                    .Where(user =>
                        user.UserName!.Contains(query) ||
                        user.FirstName.Contains(query) ||
                        user.LastName.Contains(query) ||
                        query.Contains(user.UserName) ||
                        query.Contains(user.FirstName) ||
                        query.Contains(user.LastName))
                    .OrderBy(u => u.JoinDate)
                    .ThenBy(u => u.Id)
                    .Select(u => u.ToDTO())
                    .Where(u => u.JoinDate > lastDate || (u.JoinDate == lastDate && u.Id.CompareTo(lastId) > 0))
                    .Take(10)
                    .ToListAsync();
            }

            return users;
        }
        catch (Exception ex)
        {
            logger.LogError(ex, "UsersRepository.SearchUsers: Error while searching for users with query: {query}", query);
            throw;
        }
    }

    public async Task<UserDTO> GetUser(string id)
    {
        logger.LogDebug("UsersRepository.GetUser: Getting user with id: {id}", id);
        try
        {
            var user = await userManager.FindByIdAsync(id);
            if (user == null)
            {
                logger.LogWarning("UsersRepository.GetUser: User with id: {id} not found", id);
                // throw new NotFoundException("User not found");
            }
            return user!.ToDTO();
        }
        catch (Exception ex)
        {
            logger.LogError(ex, "UsersRepository.GetUser: Error while getting user with id: {id}", id);
            throw;
        }
    }

    private async Task<UserDTO> GetUserRaw(string id)
    {
        logger.LogDebug("UsersRepository.GetUser: Getting user with id: {id}", id);
        try
        {
            var user = await userManager.FindByIdAsync(id);
            if (user == null)
            {
                logger.LogWarning("UsersRepository.GetUser: User with id: {id} not found", id);
                // throw new NotFoundException("User not found");
            }
            return user!.ToDTO();
        }
        catch (Exception ex)
        {
            logger.LogError(ex, "UsersRepository.GetUser: Error while getting user with id: {id}", id);
            throw;
        }
    }

    public async Task UpdateUser(UserDTO user)
    {
        logger.LogDebug("UsersRepository.UpdateUser: Updating user with id: {id}", user.Id);
        try
        {
            var userModel = dbContext.Users.Find(user.Id);
            userModel?.UpdateFromDTO(user);
            if (userModel != null)
            {
                dbContext.Users.Update(userModel);
                await dbContext.SaveChangesAsync();
            }
        }
        catch (Exception ex)
        {
            logger.LogError(ex, "UsersRepository.UpdateUser: Error while updating user with id: {id}", user.Id);
            throw;
        }
    }

    public async Task<IEnumerable<NotificationToken>> GetNotificationTokens(string userId)
    {
        logger.LogDebug("UsersRepository.GetNotificationTokens: Getting notification tokens of user with id: {userId}", userId);
        try
        {
            var tokens = await dbContext.Users
            .Where(u => u.Id == userId)
            .SelectMany(u => u.NotificationTokens)
            .Where(nt => EF.Functions.DateDiffDay(nt.CreatedAt, DateTime.Now) <= 30)
            .ToListAsync();
            logger.LogDebug("UsersRepository.GetNotificationTokens: Got notification tokens: {@tokens}", tokens.Count);
            return tokens;
        }
        catch (Exception ex)
        {
            logger.LogError(ex, "UsersRepository.GetNotificationTokens: Error while getting notification tokens of user with id: {userId}", userId);
            throw;
        }
    }

    public async Task<bool> IsUserNameAvailable(string userName)
    {
        logger.LogDebug("UsersRepository.IsUserNameAvailable: Checking if username {userName} is available", userName);
        try
        {
            var user = await userManager.FindByNameAsync(userName);
            return user == null;
        }
        catch (Exception ex)
        {
            logger.LogError(ex, "UsersRepository.IsUserNameAvailable: Error while checking if username {userName} is available", userName);
            throw;
        }
    }
}