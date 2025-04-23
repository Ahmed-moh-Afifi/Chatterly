using Chatterly_Backend.Data.Models;

namespace Chatterly_Backend.Data.DTOs;

public class UserDTO
{
    public required string Id { get; set; }
    public required string FirstName { get; set; }
    public required string LastName { get; set; }
    public required string UserName { get; set; }
    public required string Email { get; set; }
    public required bool Gender { get; set; }
    public DateTime JoinDate { get; set; } = DateTime.Now;
    public string? PhotoUrl { get; set; } = null;
    public bool Verified { get; set; } = false;
    public DateTime? LastOnline { get; set; }
    public int OnlineSessions { get; set; }

    public User ToModel()
    {
        return new User
        {
            Id = Id,
            FirstName = FirstName,
            LastName = LastName,
            UserName = UserName,
            Email = Email,
            Gender = Gender,
            JoinDate = JoinDate,
            PhotoUrl = PhotoUrl,
            Verified = Verified,
            LastOnline = LastOnline,
            OnlineSessions = OnlineSessions,
        };
    }
}
