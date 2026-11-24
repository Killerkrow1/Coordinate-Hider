package net.killerkrow.coordhider.mixin;

import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Mixin(DebugScreenOverlay.class)
public class ObfuscatedCordMixin {

    @Unique
    private static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\d+(?:\\.\\d+)?");

    @ModifyVariable(
            method = "renderLines",
            at = @At("HEAD"),
            argsOnly = true
    )
    private List<String> obfuscateCoordinateLines(List<String> lines) {
        List<String> modified = new ArrayList<>(lines.size());

        for (String line : lines) {
            if (isCoordinateLine(line)) {
                // This does the replacing numbers with obfuscated text
                String obfuscated = NUMBER_PATTERN.matcher(line)
                        .replaceAll(match -> "§k" + match.group() + "§r");
                modified.add(obfuscated);
            } else {
                modified.add(line);
            }
        }
        return modified;
    }

    @Unique
    private boolean isCoordinateLine(String line) {
        return line.startsWith("XYZ:")
                || line.startsWith("Block:")
                || line.startsWith("Chunk:")
                || line.startsWith("Section-relative:")
                || line.startsWith("Targeted Block:")
                || line.startsWith("Targeted Fluid:");
    }
}