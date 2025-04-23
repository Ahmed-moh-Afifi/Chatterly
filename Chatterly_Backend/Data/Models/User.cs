using Chatterly_Backend.Data.DTOs;
using Microsoft.AspNetCore.Identity;

namespace Chatterly_Backend.Data.Models
{
    public class User : IdentityUser
    {
        public required string FirstName { get; set; }
        public required string LastName { get; set; }
        public required bool Gender { get; set; }
        public DateTime JoinDate { get; set; } = DateTime.Now;
        public string? PhotoUrl { get; set; } = null;
        public bool Verified { get; set; } = false;
        public DateTime? LastOnline { get; set; }
        public int OnlineSessions { get; set; }

        public ICollection<NotificationToken> NotificationTokens { get; set; } = [];
        public virtual ICollection<Chat> Chats { get; set; } = [];
        public virtual ICollection<Message> Messages { get; set; } = [];
        public virtual ICollection<Message> MessagesMentionedIn { get; set; } = [];

        public static User FromRegisterModel(RegisterModel model)
        {
            return new User
            {
                FirstName = model.FirstName,
                LastName = model.LastName,
                Gender = model.Gender,
                Email = model.Email,
                UserName = model.Username,
                OnlineSessions = 0,
            };
        }

        public UserDTO ToDTO()
    {
        return new UserDTO
        {
            Id = Id,
            FirstName = FirstName,
            LastName = LastName,
            Gender = Gender,
            Email = Email!,
            UserName = UserName!,
            JoinDate = JoinDate,
            PhotoUrl = PhotoUrl,
            Verified = Verified,
            LastOnline = LastOnline,
            OnlineSessions = OnlineSessions,
        };
    }

    public void UpdateFromDTO(UserDTO dto)
    {
        FirstName = dto.FirstName;
        LastName = dto.LastName;
        Gender = dto.Gender;
        Email = dto.Email;
        UserName = dto.UserName;
        PhotoUrl = dto.PhotoUrl;
        LastOnline = dto.LastOnline;
        OnlineSessions = dto.OnlineSessions;
        Verified = dto.Verified;
    }
    }
}