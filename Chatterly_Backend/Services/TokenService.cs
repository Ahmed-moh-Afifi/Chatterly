using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography.X509Certificates;
using System.Text;

namespace Chatterly_Backend.Services;

public class TokenService(IConfiguration configuration, ILogger<TokenService> logger)
{
    public string GenerateAccessToken(IEnumerable<Claim> claims)
    {
        logger.LogDebug("TokenService -> GenerateAccessToken: generating access token");
        var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(configuration["ChatterlyJWTKey"]!));
        var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

        var tokenHandler = new JwtSecurityTokenHandler();
        var tokenDescriptor = new SecurityTokenDescriptor
        {
            Issuer = configuration["ChatterlyIssuer"],
            Audience = configuration["ChatterlyAudience"],
            Subject = new ClaimsIdentity(claims),
            Expires = DateTime.Now.AddMinutes(double.Parse(configuration["ChatterlyJWTExpiresInMinutes"]!)),
            SigningCredentials = creds
        };

        var token = tokenHandler.CreateToken(tokenDescriptor);
        return tokenHandler.WriteToken(token);
    }

    public string GenerateRefreshToken()
    {
        logger.LogDebug("TokenService -> GenerateRefreshToken: generating refresh token");
        return Guid.NewGuid().ToString();
    }

    public IEnumerable<Claim> GetPrincipalFromExpiredToken(string token)
    {
        logger.LogDebug("TokenService -> GetPrincipalFromExpiredToken: getting principal from expired token");
        var tokenValidationParameters = new TokenValidationParameters
        {
            ValidateIssuer = true,
            ValidateAudience = true,
            ValidateLifetime = false,
            ValidateIssuerSigningKey = true,
            ValidIssuer = configuration["ChatterlyIssuer"],
            ValidAudience = configuration["ChatterlyAudience"],
            IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(configuration["ChatterlyJWTKey"]!))
        };

        var tokenHandler = new JwtSecurityTokenHandler();
        _ = tokenHandler.ValidateToken(token, tokenValidationParameters, out SecurityToken securityToken);

        if (securityToken is not JwtSecurityToken jwtSecurityToken || !jwtSecurityToken.Header.Alg.Equals(SecurityAlgorithms.HmacSha256, StringComparison.InvariantCultureIgnoreCase))
            throw new SecurityTokenException("Invalid token");

        return jwtSecurityToken.Claims;
    }
}

