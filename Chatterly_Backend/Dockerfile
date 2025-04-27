# Use the official .NET SDK image to build
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /app

# Copy everything and restore
COPY . .
RUN dotnet restore

# Build and publish
RUN dotnet publish -c Release -o out

# Use the official runtime image
FROM mcr.microsoft.com/dotnet/aspnet:8.0
WORKDIR /app
COPY --from=build /app/out .

# Expose port
EXPOSE 5000

# Start the app
ENTRYPOINT ["dotnet", "Chatterly_Backend.dll"]
