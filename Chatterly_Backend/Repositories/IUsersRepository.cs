using Chatterly_Backend.Data.DTOs;
using Chatterly_Backend.Data.Models;

namespace Chatterly_Backend.Repositories;

public interface IUsersRepository
{
    public Task<IEnumerable<UserDTO>> SearchUsers(string query, DateTime? lastDate, string? lastId);
    public Task<UserDTO> GetUser(string id);
    public Task UpdateUser(UserDTO user);
    public Task<IEnumerable<NotificationToken>> GetNotificationTokens(string userId);
    public Task<bool> IsUserNameAvailable(string userName);
}