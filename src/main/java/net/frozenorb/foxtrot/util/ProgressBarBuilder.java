package net.frozenorb.foxtrot.util;

import java.util.Arrays;

import javafx.scene.control.ProgressBar;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

@Getter
@Setter
@Accessors(fluent = true)
public class ProgressBarBuilder {

	private int blocksToDisplay;
	private char blockChar;
	private String completedColor;
	private String uncompletedColor;

	public ProgressBarBuilder() {
		this(10);
	}

	public ProgressBarBuilder(int blocksToDisplay) {
		this(blocksToDisplay,StringEscapeUtils.unescapeJava("\u2588").charAt(0),ChatColor.GREEN.toString(),ChatColor.GRAY.toString());
	}

	public ProgressBarBuilder(int blocksToDisplay,char blockChar) {
		this(blocksToDisplay,blockChar,ChatColor.GREEN.toString(),ChatColor.GRAY.toString());
	}

	public ProgressBarBuilder(int blocksToDisplay,char blockChar,String completedColor,String uncompletedColor) {
		this.blocksToDisplay = blocksToDisplay;
		this.blockChar = blockChar;
		this.completedColor = completedColor;
		this.uncompletedColor = uncompletedColor;
	}

	public String build(double percentage) {

		final String[] blocks = new String[this.blocksToDisplay];

		Arrays.fill(blocks,this.uncompletedColor + this.blockChar);

		if (percentage > 100.0D) {
			percentage = 100.0D;
		}

		for (int i = 0; i < (percentage / 10); i++) {
			blocks[i] = this.completedColor + this.blockChar;
		}

		return StringUtils.join(blocks);
	}

	public static double percentage(int value, int goal) {
		return value > goal ? 100.0D : (((double) value / (double) goal) * 100.0D);
	}

}
