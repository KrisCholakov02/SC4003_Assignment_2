class Cholakov_Kristiyan_Kamenov_Player extends Player {
    // Previous round index
    int i;

    // History arrays
    int[] myHist, opp1Hist, opp2Hist;

    // Total scores
    int myScore = 0, opp1Score = 0, opp2Score = 0;

    // Cooperation counts
    int opponent1Coop = 0;
    int opponent2Coop = 0;

    // Trigger flag for if defection strategy is detected
    boolean triggerDefect = false;

    // Payoff matrix
    static int[][][] payoff = {
            {{6, 3}, {3, 0}},
            {{8, 5}, {5, 2}}
    };

    int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
        if (n == 0) return 0;  // Always cooperate in the first round

        // Update class variables
        updateScoresAndHistories(n, myHistory, oppHistory1, oppHistory2);

        // Calculate cooperation probabilities
        double opp1CoopProb = (double) opponent1Coop / n;
        double opp2CoopProb = (double) opponent2Coop / n;

        // Endgame strategy
        if (n > 90) {
            if (n > 107) return 1;  // Defect in the last rounds
            // Check for possible defection strategy
            if ((opp1Hist[n - 1] == 1 || opp2Hist[n - 1] == 1) && !(opp1Hist[n - 2] == 1 || opp2Hist[n - 2] == 1 || myHist[n - 2] == 1)) {
                triggerDefect = true;  // Trigger unconditional defection
            }
        }

        // Check for possible defection strategy
        if (n > 10) {
            if (opp1CoopProb < 0.1 || opp2CoopProb < 0.1) {
                triggerDefect = true;  // Trigger unconditional defection
            }
        }

        // If trigger is active, defect
        if (triggerDefect) {
            return 1;
        }

        // Check cooperation rate and defect if my score is lower than both opponents
        if ((opp1CoopProb < 0.5 || opp2CoopProb < 0.5) && (myScore < opp1Score || myScore < opp2Score)) {
            return 1;  // Defect if any opponent is less cooperative
        }

        // Respond based on the combined last action of opponents
        if (opp1Hist[n - 1] == 0 && opp2Hist[n - 1] == 0 && myHist[n - 1] == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    // Update scores and histories
    void updateScoresAndHistories(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
        this.i = n - 1;
        this.myHist = myHistory;
        this.opp1Hist = oppHistory1;
        this.opp2Hist = oppHistory2;
        updateScores();
        updateCooperationCounts();
    }

    // Update scores based on the payoff matrix
    void updateScores() {
        myScore += payoff[myHist[i]][opp1Hist[i]][opp2Hist[i]];
        opp1Score += payoff[opp1Hist[i]][myHist[i]][opp2Hist[i]];
        opp2Score += payoff[opp2Hist[i]][myHist[i]][opp1Hist[i]];
    }

    // Update cooperation counts
    void updateCooperationCounts() {
        opponent1Coop += opp1Hist[i] == 0 ? 1 : 0;
        opponent2Coop += opp2Hist[i] == 0 ? 1 : 0;
    }
}