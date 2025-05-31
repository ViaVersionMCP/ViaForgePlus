package net.aspw.viaforgeplus;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.ProfileNotFoundException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.viaversion.viaversion.api.minecraft.GameProfile;
import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import net.aspw.viaforgeplus.common.platform.VFPlatform;
import net.minecraft.realms.RealmsSharedConstants;
import net.minecraft.util.Session;
import net.minecraftforge.fml.common.Mod;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.provider.GameProfileFetcher;

import java.io.File;
import java.net.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Mod(modid = "viaforgeplus", name = "ViaForgePlus", acceptableRemoteVersions = "*", clientSideOnly=true, useMetadata=true)
public class ViaForgePlus implements VFPlatform {

    public void initVFPlatform() {
        CommonViaForgePlus.init(this);
    }

    @Override
    public int getGameVersion() {
        return RealmsSharedConstants.NETWORK_PROTOCOL_VERSION;
    }

    @Override
    public File getLeadingDirectory() {
        return IMinecraft.mc.mcDataDir;
    }

    @Override
    public void joinServer(String serverId) throws Throwable {
        final Session session = IMinecraft.mc.getSession();

        IMinecraft.mc.getSessionService().joinServer(session.getProfile(), session.getToken(), serverId);
    }

    public static final HttpAuthenticationService AUTHENTICATION_SERVICE = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
    public static final MinecraftSessionService SESSION_SERVICE = AUTHENTICATION_SERVICE.createMinecraftSessionService();
    public static final GameProfileRepository GAME_PROFILE_REPOSITORY = AUTHENTICATION_SERVICE.createProfileRepository();

    @Override
    public GameProfileFetcher getGameProfileFetcher() {
        return new GameProfileFetcher() {
            @Override
            public UUID loadMojangUuid(final String playerName) throws Exception {
                final CompletableFuture<com.mojang.authlib.GameProfile> future = new CompletableFuture<>();
                GAME_PROFILE_REPOSITORY.findProfilesByNames(new String[]{playerName}, Agent.MINECRAFT, new ProfileLookupCallback() {
                    @Override
                    public void onProfileLookupSucceeded(com.mojang.authlib.GameProfile profile) {
                        future.complete(profile);
                    }

                    @Override
                    public void onProfileLookupFailed(com.mojang.authlib.GameProfile profile, Exception exception) {
                        future.completeExceptionally(exception);
                    }
                });
                if (!future.isDone()) {
                    future.completeExceptionally(new ProfileNotFoundException());
                }
                return future.get().getId();
            }

            @Override
            public GameProfile loadGameProfile(final UUID uuid) throws Exception {
                /*final com.mojang.authlib.GameProfile inProfile = new com.mojang.authlib.GameProfile(uuid, null);
                final com.mojang.authlib.GameProfile mojangProfile = SESSION_SERVICE.fillProfileProperties(inProfile, true);
                if (mojangProfile.equals(inProfile)) throw new ProfileNotFoundException();

                final GameProfile gameProfile = new GameProfile(mojangProfile.getName(), mojangProfile.getId());
                for (final java.util.Map.Entry<String, Property> entry : mojangProfile.getProperties().entries()) {
                    final Property prop = entry.getValue();
                    gameProfile.addProperty(new GameProfile.Property(prop.getName(), prop.getValue(), prop.getSignature()));
                }

                return gameProfile;*/
                return null;
            }
        };
    }
}
