using Chatterly_Backend.Data.Models;

namespace Chatterly_Backend.Data.DTOs
{
    public class MessageDTO
    {
        public required int Id { get; set; }
        public required string Uid { get; set; }
        public required string Body { get; set; }
        public List<string> Images { get; set; } = [];
        public List<string> Videos { get; set; } = [];
        public List<string> Audios { get; set; } = [];
        public DateTime PublishDate { get; set; } = DateTime.Now;
        public int? MessageRepliedTo { get; set; }
        public required string AuthorId { get; set; }
        public int ReactionsCount { get; set; }
        public int RepliesCount { get; set; }
        public required int ChatId { get; set; }
    }
}
