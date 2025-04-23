using System;

namespace Chatterly_Backend.Data.Models
{
    public class ResetPasswordModel : LoginModel
    {
        public required string NewPassword { get; set; }
    }
}
