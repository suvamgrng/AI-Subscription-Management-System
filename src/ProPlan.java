
public class ProPlan extends AIModel {
    private int teamSlot;
    private final int maxSlot;

    public ProPlan(String var1, double var2, int var4, int var5, int var6) {
        super(var1, var2, var4, var5);
        this.teamSlot = var6;
        this.maxSlot = var6;
    }

    public int getTeamSlot() {
        return this.teamSlot;
    }

    public String addTeamMember(String var1) {
        if (this.teamSlot == 0) {
            return "Error: No available slots. Please upgrade your plan.";
        } else {
            --this.teamSlot;
            return "Success! " + var1 + " added. Remaining slots: " + this.teamSlot;
        }
    }

    public String removeTeamMember(String var1) {
        if (this.teamSlot >= this.maxSlot) {
            return "Error: No members to remove. Slots already at maximum.";
        } else {
            ++this.teamSlot;
            return "Success! " + var1 + " removed. Available slots: " + this.teamSlot;
        }
    }

    public String display() {
        String var10000 = super.display();
        return var10000 + "\nPlan Type: Pro Plan\nAvailable Team Slots: " + this.teamSlot + "\n(Pro Plan: No quota deducted!)";
    }
}
