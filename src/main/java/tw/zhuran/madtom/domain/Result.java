package tw.zhuran.madtom.domain;

public class Result {
    private ResultType resultType;
    private int winner;
    private int loser;
    private Score score;
    private Plot plot;

    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public int getLoser() {
        return loser;
    }

    public void setLoser(int loser) {
        this.loser = loser;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public Plot getPlot() {
        return plot;
    }

    public void setPlot(Plot plot) {
        this.plot = plot;
    }
}
