public class Debug {
    private static boolean  enabled;

    public Debug() {
        enabled = true;
    }

    public Debug(boolean enable) {
        enabled = enable;
    }

    public void println(String text) {
        if (enabled)
            System.out.println("DEBUG: " + text);
    }

    public void setEnabled(boolean enable) {
        enabled = enable;
    }
}
