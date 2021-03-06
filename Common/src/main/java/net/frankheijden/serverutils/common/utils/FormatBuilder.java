package net.frankheijden.serverutils.common.utils;

import java.util.ArrayList;
import java.util.List;
import net.frankheijden.serverutils.common.config.Messenger;
import net.frankheijden.serverutils.common.entities.ServerCommandSender;

public class FormatBuilder {

    private final String format;
    private final List<String[]> valueList;
    private String[] orderedKeys;
    private boolean alwaysSend;

    private FormatBuilder(String format) {
        this.format = format;
        this.valueList = new ArrayList<>();
        this.orderedKeys = new String[0];
        this.alwaysSend = false;
    }

    public static FormatBuilder create(String format) {
        return new FormatBuilder(format);
    }

    public FormatBuilder orderedKeys(String... orderedKeys) {
        this.orderedKeys = orderedKeys;
        return this;
    }

    public FormatBuilder add(String... values) {
        this.valueList.add(values);
        return this;
    }

    public FormatBuilder alwaysSend(boolean alwaysSend) {
        this.alwaysSend = alwaysSend;
        return this;
    }

    /**
     * Builds the format and sends it to the CommandSender.
     * @param sender The receiver of the list.
     */
    public void sendTo(ServerCommandSender sender) {
        valueList.forEach(values -> {
            int length = Math.min(values.length, orderedKeys.length);
            String message = format;
            for (int i = 0; i < length; i++) {
                String value = values[i];
                if ((value == null || value.isEmpty()) && !alwaysSend) return;
                message = message.replace(orderedKeys[i], String.valueOf(value));
            }
            Messenger.sendRawMessage(sender, message);
        });
    }
}
