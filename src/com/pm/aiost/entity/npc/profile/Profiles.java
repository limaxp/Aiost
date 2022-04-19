package com.pm.aiost.entity.npc.profile;

import java.util.Random;

import com.mojang.authlib.GameProfile;
import com.pm.aiost.misc.registry.AiostRegistry;

public class Profiles {

	public static final GameProfile ORC = a("Orc",
			"HpXgIUc3rC56btSQ7x8VhtXkn3Sf9+ZjHlydLaFuo27erC9gFQi4g8dLDvifJF1n79tFp13BmHAeuMVjiG0g7ZzMPg56ITLHhtU+ZZrmf9C7H2yvcbbs0X2VVaMLZe9qPDIWGttmKe1EaeFabFgdb4ynQoLyKDJDBKTo5ONDYTWYNVrdK2pkW46Pkaoidq7EuSNm3cgXoGsBQO0R5t4576Z7TIJUh4TgsgKYs3mUg9iy0JilJUrWeRqJOFWcFXDDUwenKNXI7FBbmqyxKAmZM4DIpGaw/D0u8ijQDCdQ5Wymxpo1UCn6ZMLKzFGuK4oSCofdsydF4LWwKAZJU3s8n4sFT25ALGjfFTCPFkzUVSMd8MpputO/uPoQCldswkN9Gqtq8L12OhxCRib2x8wa7NtDAjSRGbqGvVCpwEIf9wI194VpMG87k9MmQwtR4l8yvTlen3DoAAyhIRyltuOe5aPp11O4Bxd3o9aRcVB8QxqFOQ6DyLm4CGhBd0SmW8CekuJupWlwKB5Ob9tp4FVQk3+TS+71TYKXqWQ6QvFR0HD6BHHhjBzfieMglnHw9ObsOZfA2Udhj/DUvB0JFnGx9XAzrcLDRb8xWn48rQ19drz60pqJeEhp8xAG+H+i3uMqjYktCs1KT1bvfYIBUaKnEo5Dbz1fzzlBEfcHgSqf5F0=",
			"ewogICJ0aW1lc3RhbXAiIDogMTYwNDUzNTczMjc4NSwKICAicHJvZmlsZUlkIiA6ICJhYTZhNDA5NjU4YTk0MDIwYmU3OGQwN2JkMzVlNTg5MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJiejE0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzEyZTgxM2E2MzcxNzEzYzljNzc0ZTc0MjBiZTMzZTZhMzkxZmExOGM5OGM2ZjlkYTA3NzQyMWQ2MDdiYWU0ZjkiCiAgICB9CiAgfQp9");

	public static GameProfile a(String name, String skinSignature, String skin) {
		return a(name, ProfileBuilder.create(name, skinSignature, skin));
	}

	public static GameProfile a(String name, String skinSignature, String skin, String capeSignature, String cape) {
		return a(name, ProfileBuilder.create(name, skinSignature, skin, capeSignature, cape));
	}

	public static GameProfile a(String name, GameProfile profile) {
		AiostRegistry.PROFILES.register(name, profile);
		return profile;
	}

	private static final Random RANDOM = new Random();

	public static GameProfile getRandom() {
		return AiostRegistry.PROFILES.get(RANDOM.nextInt(AiostRegistry.PROFILES.size()));
	}

	public static GameProfile get(String name) {
		GameProfile profile = AiostRegistry.PROFILES.get(name);
		if (profile == null)
			return ProfileFetcher.fetch(name, false);
		return profile;
	}
}