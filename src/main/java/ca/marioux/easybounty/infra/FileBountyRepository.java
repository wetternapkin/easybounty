package ca.marioux.easybounty.infra;

import ca.marioux.easybounty.domain.Bounty;
import ca.marioux.easybounty.domain.BountyId;
import ca.marioux.easybounty.domain.BountyRepository;
import ca.marioux.easybounty.domain.PlayerId;
import ca.marioux.easybounty.domain.exception.BountyAlreadyExistsException;
import ca.marioux.easybounty.domain.exception.BountyDoesntExistsException;
import ca.marioux.easybounty.domain.exception.RepositoryException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileBountyRepository implements BountyRepository {

    private final File bountiesFile;

    public FileBountyRepository(File bountiesFile) {
        this.bountiesFile = bountiesFile;
    }

    private YamlConfiguration loadFile() {
        final YamlConfiguration ymlFile = YamlConfiguration.loadConfiguration(bountiesFile);
        ymlFile.addDefault("bounties", new Object[]{});

        return ymlFile;
    }

    private void saveFile(YamlConfiguration ymlFile) throws IOException {
        ymlFile.save(bountiesFile);
    }

    @Override
    public void add(Bounty bounty) throws BountyAlreadyExistsException {
        YamlConfiguration ymlFile = loadFile();

        String benefactor = bounty.getBenefactor() == null
                ? null
                : bounty.getBenefactor().getUuid().toString();
        String target = bounty.getTarget().toString();
        ItemStack reward = bounty.getReward();

        Object existing = ymlFile.get(String.format("bounties.%s.%s", target, benefactor));
        if (existing != null) {
            throw new BountyAlreadyExistsException();
        }

        RewardRecord record = new RewardRecord(
                reward.getType().name(),
                reward.getAmount());

        ymlFile.set(String.format("bounties.%s.%s", target, benefactor), record.asList());

        try {
            saveFile(ymlFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Bounty remove(BountyId bountyId) throws BountyDoesntExistsException {
        YamlConfiguration ymlFile = loadFile();

        String pathToBounty = String.format("bounties.%s.%s", bountyId.getTarget().toString(), bountyId.getBenefactor().toString());

        List record = ymlFile.getList(pathToBounty);
        if (record == null) {
            throw new BountyDoesntExistsException();
        }

        ymlFile.set(pathToBounty, null);

        try {
            saveFile(ymlFile);
        } catch (IOException e) {
            throw new RepositoryException();
        }

        return new RewardRecord(record).toBounty(bountyId);
    }

    @Override
    public List<Bounty> removeByTarget(PlayerId targetId) {
        YamlConfiguration ymlFile = loadFile();

        MemorySection existing = (MemorySection) ymlFile.get(String.format("bounties.%s", targetId.toString()));
        if (existing == null) {
            return new ArrayList<Bounty>();
        }

        Set<String> allBenefactors = existing.getKeys(true);

        List<Bounty> bounties = new ArrayList<Bounty>();

        for (String benefactor : allBenefactors) {
            List record = ymlFile.getList(String.format("bounties.%s.%s", targetId.toString(), benefactor));

            BountyId id = new BountyId(PlayerId.fromString(benefactor), targetId);
            Bounty bounty = new RewardRecord(record).toBounty(id);

            bounties.add(bounty);
        }

        ymlFile.set(String.format("bounties.%s", targetId.toString()), null);

        try {
            saveFile(ymlFile);
        } catch (IOException e) {
            throw new RepositoryException();
        }

        return bounties;
    }
}
