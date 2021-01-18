package ca.marioux.easybounty.infra;

import ca.marioux.easybounty.domain.PlayerId;
import ca.marioux.easybounty.domain.Refund;
import ca.marioux.easybounty.domain.RefundRepository;
import ca.marioux.easybounty.domain.exception.RefundNotFoundException;
import ca.marioux.easybounty.domain.exception.RepositoryException;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileRefundRepository implements RefundRepository {

    private final File file;

    public FileRefundRepository(File file) {
        this.file = file;
    }

    private YamlConfiguration loadFile() {
        final YamlConfiguration ymlFile = YamlConfiguration.loadConfiguration(file);
        ymlFile.addDefault("refunds", new Object[]{});

        return ymlFile;
    }

    private void saveFile(YamlConfiguration ymlFile) throws IOException {
        ymlFile.save(file);
    }

    @Override
    public void add(Refund refund) {
        YamlConfiguration yml = loadFile();

        List<Map<?, ?>> rewards = yml.getMapList(String.format("refunds.%s", refund.getTarget().toString()));
        if (rewards == null) {
            rewards = new ArrayList<Map<?, ?>>();
        }

        Map<String, Object> newRefund = new HashMap<String, Object>();
        newRefund.put("material", refund.getReward().getType().name());
        newRefund.put("amount", refund.getReward().getAmount());

        rewards.add(newRefund);

        yml.set(String.format("refunds.%s", refund.getTarget().toString()), rewards);

        try {
            saveFile(yml);
        } catch (IOException e) {
            throw new RepositoryException();
        }
    }

    @Override
    public List<Refund> removeAll(PlayerId target) {
        YamlConfiguration yml = loadFile();

        List<Map<?, ?>> rewards = yml.getMapList(String.format("refunds.%s", target.toString()));
        if (rewards == null || rewards.size() == 0) {
            return new ArrayList<Refund>();
        }

        List<Refund> refunds = new ArrayList<Refund>();
        for (Map<?, ?> serializedReward : rewards) {
            String material = (String) serializedReward.get("material");
            int amount = (Integer) serializedReward.get("amount");

            ItemStack item = new ItemStack(Material.getMaterial(material), amount);
            Refund refund = new Refund(target, item);

            refunds.add(refund);
        }

        yml.set(String.format("refunds.%s", target.toString()), null);

        try {
            saveFile(yml);
        } catch (IOException e) {
            throw new RepositoryException();
        }

        return refunds;
    }
}
