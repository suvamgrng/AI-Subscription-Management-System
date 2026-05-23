
public class PersonalPlan extends AIModel {
    private int promptsRemaining;

    public PersonalPlan(String var1, double var2, int var4, int var5, int var6) {
        super(var1, var2, var4, var5);
        this.promptsRemaining = var6;
    }

    public int getPromptsRemaining() {
        return this.promptsRemaining;
    }

    public String purchasePrompts(int var1) {
        if (var1 < 0) {
            return "Purchase Prompt Error: \"You must enter positive value or must upgrade to pro plan\"";
        } else {
            this.promptsRemaining += var1;
            return "Success! " + var1 + " prompts added. Total remaining now: " + this.promptsRemaining + ".";
        }
    }

    public String enterPrompt(String var1, int var2) {
        if (var2 <= 0) {
            return "Invalid token length. Please enter a positive value.";
        } else {
            int var3 = this.getContextWindow() * 1000;
            int var4 = var1.length();
            byte var5 = 100;
            int var6 = var4 + var2 + var5;
            if (var6 > var3) {
                return "Error occurred! Total tokens (" + var6 + ") exceed context window " + this.getContextWindow() + " (" + var3 + " tokens)\nInput tokens: " + var4 + "\nOutput tokens: " + var2 + "\nSystem tokens: " + var5;
            } else if (this.promptsRemaining > 0) {
                --this.promptsRemaining;
                return "Prompt sent: " + var1 + "\nTotal tokens used: " + var6 + "\nRemaining prompts: " + this.promptsRemaining;
            } else {
                return "Monthly quota reached! Please purchase more prompts.";
            }
        }
    }

    public String display() {
        String var10000 = super.display();
        return var10000 + "\nPlan Type: Personal Plan\nPrompts Remaining: " + this.promptsRemaining;
    }
}
