﻿using Microsoft.AspNetCore.SignalR;
using System.Security.Claims;

namespace Chatterly_Backend.Hubs
{
    public class CustomUserIdProvider : IUserIdProvider
    {
        public string? GetUserId(HubConnectionContext connection)
        {
            return connection.User?.FindFirst("uid")?.Value;
        }
    }

}
