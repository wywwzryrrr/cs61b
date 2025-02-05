public class Dogs {
    public int weighInPounds;

    public Dogs(int w) {
        weighInPounds = w;
    }

    public void makeNoise() {
        if (weighInPounds < 10) {
            System.out.println("yipyipyip!");
        } else if (weighInPounds < 30) {
            System.out.println("bark.");
        } else {
            System.out.println("woof!");
        }
    }
}

class DogsLauncher {
    public static void main(String[] args) {
        Dogs d = new Dogs(10);
        d.makeNoise();
    }
}