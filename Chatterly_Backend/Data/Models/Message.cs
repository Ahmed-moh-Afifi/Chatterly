﻿using Chatterly_Backend.Data.DTOs;

namespace Chatterly_Backend.Data.Models
{
    public class Message
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

        public virtual User? Author { get; set; }
        public virtual Chat? Chat { get; set; }
        public virtual ICollection<MessageReaction> Reactions { get; set; } = [];
        public virtual ICollection<User> Mentions { get; set; } = [];

        public MessageDTO ToDTO()
        {
            return new MessageDTO
            {
                Id = Id,
                Uid = Uid,
                Body = Body,
                Images = Images,
                Videos = Videos,
                Audios = Audios,
                PublishDate = PublishDate,
                MessageRepliedTo = MessageRepliedTo,
                AuthorId = AuthorId,
                ReactionsCount = ReactionsCount,
                RepliesCount = RepliesCount,
                ChatId = ChatId,
            };
        }
    }
}
