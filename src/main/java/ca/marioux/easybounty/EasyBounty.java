package ca.marioux.easybounty;

import ca.marioux.easybounty.application.BountyService;
import ca.marioux.easybounty.application.GiveItemService;
import ca.marioux.easybounty.commands.CommandBounty;
import ca.marioux.easybounty.commands.TabCompletion;
import ca.marioux.easybounty.domain.BountyRepository;
import ca.marioux.easybounty.domain.RefundRepository;
import ca.marioux.easybounty.infra.FileBountyRepository;
import ca.marioux.easybounty.infra.FileRefundRepository;
import ca.marioux.easybounty.infra.RewardRecord;
import ca.marioux.easybounty.listeners.KillListener;
import ca.marioux.easybounty.listeners.PlayerConnectListener;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class EasyBounty extends JavaPlugin {

    private static final String BOUNTIES_FILE_NAME = "bounties.yml";
    private static final String REFUND_FILE_NAME = "bounties.yml";


    @Override
    public void onEnable() {
        BountyRepository bountyRepository = null;

        try {
            bountyRepository = createBountyRepository();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RefundRepository refundRepository = null;

        try {
            refundRepository = createRefundRepository();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BountyService bountyService = new BountyService(bountyRepository, refundRepository, new GiveItemService(refundRepository));

        getCommand("bounty").setExecutor(new CommandBounty(bountyService));
        getCommand("bounty").setTabCompleter(new TabCompletion());

        getServer().getPluginManager().registerEvents(new KillListener(bountyService), this);
        getServer().getPluginManager().registerEvents(new PlayerConnectListener(refundRepository), this );

        getLogger().info("EasyBounty enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("EasyBounty disabled");
    }

    private BountyRepository createBountyRepository() throws IOException {
        final File bountiesFile = getConfigFile(BOUNTIES_FILE_NAME);

        return new FileBountyRepository(bountiesFile);
    }

    private RefundRepository createRefundRepository() throws IOException {
        final File bountiesFile = getConfigFile(REFUND_FILE_NAME);

        return new FileRefundRepository(bountiesFile);
    }

    private File getConfigFile(String bountiesFileName) throws IOException {
        final File pathToFile = getDataFolder();
        if (!pathToFile.exists()) {
            pathToFile.mkdir();
        }

        final File bountiesFile = new File(pathToFile, bountiesFileName);
        if (!bountiesFile.exists()) {
            bountiesFile.createNewFile();
        }

        return bountiesFile;
    }

}
