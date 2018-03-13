package tw.zhuran.madtom.util;

import tw.zhuran.madtom.domain.Plot;
import tw.zhuran.madtom.domain.Result;
import tw.zhuran.madtom.domain.ResultType;
import tw.zhuran.madtom.domain.Score;

public class Results {
    public static Result win(int winner, Plot plot, Score score) {
        Result result = new Result();
        result.setResultType(ResultType.WIN);
        result.setWinner(winner);
        result.setPlot(plot);
        result.setScore(score);
        return result;
    }

    public static Result draw() {
        Result result = new Result();
        result.setResultType(ResultType.DRAW);
        return result;
    }
}
