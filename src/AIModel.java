
public class AIModel {
    private String modelName;
    private double price;
    private int parameterCount;
    private int contextWindow;

    public AIModel(String var1, double var2, int var4, int var5) {
        this.modelName = var1;
        this.price = var2;
        this.parameterCount = var4;
        this.contextWindow = var5;
    }

    public String getModelName() {
        return this.modelName;
    }

    public double getPrice() {
        return this.price;
    }

    public int getParameterCount() {
        return this.parameterCount;
    }

    public int getContextWindow() {
        return this.contextWindow;
    }

    public String display() {
        return "Model Name: " + this.modelName + "\nPrice (per 1 lakh tokens): Rs." + this.price + "\nParameter Count: " + this.parameterCount + " billion\nContext Window: " + this.contextWindow + "K tokens";
    }
}
