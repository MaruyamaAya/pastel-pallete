package backend;

class GestureChooser {
    /**
     * TODO: 根据情感分析结果选出表情编号
     * @param emotion   emotion object
     * @return          gesture id
     */
    static int chooseGesture(Emotion emotion) {
        switch (emotion.level1) {
            case OPTIMISTIC:
                return 0;
            case NEUTRAL:
                return 1;
            case PESSIMISTIC:
                return 2;
            default:
        }
        return 0;
    }
}
