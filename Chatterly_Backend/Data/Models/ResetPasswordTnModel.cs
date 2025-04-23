using System;

namespace Chatterly_Backend.Data.Models
{
    public class ResetPasswordTnModel : ForgetPasswordModel
    {
        public required string Token { get; set; }
        public required string NewPassword { get; set; }
    }
}
