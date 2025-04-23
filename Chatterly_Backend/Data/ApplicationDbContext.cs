using Chatterly_Backend.Data.Models;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;

namespace Chatterly_Backend.Data
{
    public class ApplicationDbContext(DbContextOptions<ApplicationDbContext> options) : IdentityDbContext<User>(options)
    {
        public DbSet<RefreshToken> RefreshTokens { get; set; }
        public DbSet<NotificationToken> NotificationTokens { get; set; }
        public DbSet<Chat> Chats { get; set; }
        public DbSet<Message> Messages { get; set; }
        public DbSet<MessageReaction> MessagesReactions { get; set; }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);
            builder.Entity<User>().ToTable("Users");
            builder.Entity<NotificationToken>().ToTable("NotificationTokens").HasOne(nt => nt.User).WithMany(u => u.NotificationTokens).HasForeignKey("UserId").OnDelete(DeleteBehavior.NoAction);
            builder.Entity<Chat>().ToTable("Chats").HasMany(c => c.Users).WithMany(u => u.Chats);
            builder.Entity<Message>().ToTable("Messages").HasOne(m => m.Chat).WithMany(c => c.Messages).OnDelete(DeleteBehavior.NoAction);
            builder.Entity<Message>().ToTable("Messages").HasMany(m => m.Reactions).WithOne(r => r.Message).OnDelete(DeleteBehavior.NoAction);
            builder.Entity<Message>().ToTable("Messages").HasOne(m => m.Author).WithMany(u => u.Messages).OnDelete(DeleteBehavior.NoAction);
            builder.Entity<Message>().ToTable("Messages").HasMany(m => m.Mentions).WithMany(u => u.MessagesMentionedIn);
        }
    }
}