package io.github.htlsne.optimizepayment.model;

public class MoneySet {

    public static final int NUMBER_OF_COIN_TYPES = 9;
    private int c1;
    private int c5;
    private int c10;
    private int c50;
    private int c100;
    private int c500;
    private int b1000;
    private int b5000;
    private int b10000;

    public int getC1() {
        return c1;
    }

    public int getC5() {
        return c5;
    }

    public int getC10() {
        return c10;
    }

    public int getC50() {
        return c50;
    }

    public int getC100() {
        return c100;
    }

    public int getC500() {
        return c500;
    }

    public int getB1000() {
        return b1000;
    }

    public int getB5000() {
        return b5000;
    }

    public int getB10000() {
        return b10000;
    }

    public void setC1(int c1) {
        this.c1 = c1;
    }

    public void setC5(int c5) {
        this.c5 = c5;
    }

    public void setC10(int c10) {
        this.c10 = c10;
    }

    public void setC50(int c50) {
        this.c50 = c50;
    }

    public void setC100(int c100) {
        this.c100 = c100;
    }

    public void setC500(int c500) {
        this.c500 = c500;
    }

    public void setB1000(int b1000) {
        this.b1000 = b1000;
    }

    public void setB5000(int b5000) {
        this.b5000 = b5000;
    }

    public void setB10000(int b10000) {
        this.b10000 = b10000;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        int[] faceAmountArr = MoneySet.getFaceAmountArray();
        int[] numbersOfCoinsArr = this.getNumbersOfCoins();

        for (int i = 0; i < NUMBER_OF_COIN_TYPES; i++) {
            sb.append(faceAmountArr[i]);
            sb.append(": ");
            sb.append(numbersOfCoinsArr[i]);
            sb.append(", ");
        }

        return sb.toString();
    }

    public String toShortString() {
        StringBuilder sb = new StringBuilder();

        int[] faceAmountArr = MoneySet.getFaceAmountArray();
        int[] numbersOfCoinsArr = this.getNumbersOfCoins();

        for (int i = 0; i < NUMBER_OF_COIN_TYPES; i++) {
            if (numbersOfCoinsArr[i] > 0) {
                sb.append(faceAmountArr[i]);
                sb.append(": ");
                sb.append(numbersOfCoinsArr[i]);
                sb.append(", ");
            }
        }

        String string = sb.toString();
        if ("".equals(string)) {
            return "0";
        } else {
            return string;
        }
    }

    // コインの数の配列を返す
    public int[] getNumbersOfCoins() {
        int[] result = new int[NUMBER_OF_COIN_TYPES];
        result[0] = this.getC1();
        result[1] = this.getC5();
        result[2] = this.getC10();
        result[3] = this.getC50();
        result[4] = this.getC100();
        result[5] = this.getC500();
        result[6] = this.getB1000();
        result[7] = this.getB5000();
        result[8] = this.getB10000();

        return result;
    }

    public int[] getReversedNumbersOfCoins() {
        int[] normal = this.getNumbersOfCoins();
        int[] reversed = new int[NUMBER_OF_COIN_TYPES];

        for (int i = 0; i < NUMBER_OF_COIN_TYPES; i++) {
            reversed[i] = normal[NUMBER_OF_COIN_TYPES - 1 - i];
        }

        return reversed;
    }

    // コインの数を配列でセットする
    public void setFromArray(int[] arr) {
        this.setC1(arr[0]);
        this.setC5(arr[1]);
        this.setC10(arr[2]);
        this.setC50(arr[3]);
        this.setC100(arr[4]);
        this.setC500(arr[5]);
        this.setB1000(arr[6]);
        this.setB5000(arr[7]);
        this.setB10000(arr[8]);
    }

    public int getAmount() {
        int amount = 0;
        int[] faceAmountArr = MoneySet.getFaceAmountArray();
        int[] numbersOfCoins = this.getNumbersOfCoins();

        for (int i = 0; i < NUMBER_OF_COIN_TYPES; i++) {
            amount += faceAmountArr[i] * numbersOfCoins[i];
        }
        return amount;
    }

    // 額面の入った配列を返す
    public static int[] getFaceAmountArray() {
        int[] arr = {1, 5, 10, 50, 100, 500, 1000, 5000, 10000};
        return arr;
    }

    public static int[] getReversedFaceAmountArray() {
        int[] arr = {10000, 5000, 1000, 500, 100, 50, 10, 5, 1};
        return arr;
    }

    // intからそのamountを持つMoneySetを作る
    public static MoneySet valueOf(int n) {
        MoneySet result = new MoneySet();

        int[] faceAmountArr = MoneySet.getFaceAmountArray();
        int[] setterArr = new int[NUMBER_OF_COIN_TYPES];

        int rest = n;
        for (int i = NUMBER_OF_COIN_TYPES - 1; i >= 0; i--) {
            int numberOfCoin = rest / faceAmountArr[i]; // 枚数を計算
            setterArr[i] = numberOfCoin;
            rest = rest % faceAmountArr[i];
        }

        result.setFromArray(setterArr);

        return result;
    }

    // thisにaddendを足す
    // コインやお札の枚数を足すだけ
    public void add(MoneySet addend) {
        int[] thisNumbersOfCoins = this.getNumbersOfCoins();
        int[] addendNumbersOfCoins = addend.getNumbersOfCoins();
        int[] setterArr = new int[NUMBER_OF_COIN_TYPES];

        for (int i = 0; i < NUMBER_OF_COIN_TYPES; i++) {
            int sum = thisNumbersOfCoins[i] + addendNumbersOfCoins[i];
            setterArr[i] = sum;
        }
        this.setFromArray(setterArr);
    }

    // 最適化したものを返す（例：1円玉7枚 -> 1円玉2枚 + 5円玉1枚）
    public MoneySet getOptimizedSet() {
        return MoneySet.valueOf(this.getAmount());
    }

    // 最適な支払い方法を返す
    // もしお金が足りなければnull
    public MoneySet getSetForPayment(int payment) {
        if (this.getAmount() < payment) {
            return null;
        }

        MoneySet result = new MoneySet();
        int[] thisNumbersOfCoins = this.getNumbersOfCoins();
        int[] faceAmountArr = MoneySet.getFaceAmountArray();
        int[] setterArr = new int[NUMBER_OF_COIN_TYPES];

        int rest = payment;
        for (int i = 0; i < NUMBER_OF_COIN_TYPES - 1; i++) {   // 最後の1つ手前までループ
            int currentNum = thisNumbersOfCoins[i];
            int currentFace = faceAmountArr[i];
            int nextFace = faceAmountArr[i + 1];

            int currentPartOfPayment = rest % nextFace;
            if (currentPartOfPayment <= currentFace * currentNum) {     // 支払えるとき
                setterArr[i] = currentPartOfPayment / currentFace;
                rest -= currentPartOfPayment;               // 支払った分は除く
            } else {                                        // 支払えないとき
                rest += nextFace - currentPartOfPayment;    // お釣りをもらって繰上げ
            }
        }

        setterArr[NUMBER_OF_COIN_TYPES - 1] = rest / 10000;     // 10000は別に処理

        result.setFromArray(setterArr);

        return result;
    }

}
