package codes.colmenares.minecraft.packupdater.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import javax.annotation.Nullable;
import java.io.IOException;

public class UpdateGUI extends GuiScreen
{
    private final GuiScreen parent;
    private GuiButton continueAnyway;

    public UpdateGUI(@Nullable GuiScreen parent)
    {
        this.parent = parent;
    }

    @Override
    public void initGui()
    {
        ScaledResolution sr = new ScaledResolution(this.mc);

        this.continueAnyway = new GuiButton(10, (sr.getScaledWidth() / 2) - 90, 190, 180, 20, "Continue");

        this.buttonList.add(this.continueAnyway);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        String title = "Today's your lucky day!";
        String[] description;

        description = ("Rebirth of the Night has a new update!\n"
                + "This is a test gui lol\n\n\n").split("\n");

        ScaledResolution screenRes = new ScaledResolution(this.mc);

        this.drawDefaultBackground();
        this.drawString(this.fontRenderer, title, (screenRes.getScaledWidth() / 2) - (this.fontRenderer.getStringWidth(title) / 2), 50, 0xFFFFFF);

        for (int i = 0; i < description.length; i++)
        {
            String str = description[i];

            this.drawString(this.fontRenderer, str, (screenRes.getScaledWidth() / 2) - (this.fontRenderer.getStringWidth(str) / 2), 70 + (i * 10), 0xFFFFFF);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == this.continueAnyway.id)
        {
            ClientEventHandler.showGUI = false;

            this.mc.displayGuiScreen(this.parent);
        }

        super.actionPerformed(button);
    }


}