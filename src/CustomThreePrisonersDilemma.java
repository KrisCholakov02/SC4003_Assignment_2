package src;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.*;


public class CustomThreePrisonersDilemma {

    /*
     * This Java program models the two-player Prisoner's Dilemma game. We use the
     * integer "0" to represent cooperation, and "1" to represent defection.
     *
     * Recall that in the 2-players dilemma, U(DC) > U(CC) > U(DD) > U(CD), where we
     * give the payoff for the first player in the list. We want the three-player
     * game to resemble the 2-player game whenever one player's response is fixed,
     * and we also want symmetry, so U(CCD) = U(CDC) etc. This gives the unique
     * ordering
     *
     * U(DCC) > U(CCC) > U(DDC) > U(CDC) > U(DDD) > U(CDD)
     *
     * The payoffs for player 1 are given by the following matrix:
     */

    static int[][][] payoff = {
            {{6, 3}, // payoffs when first and second players cooperate
                    {3, 0}}, // payoffs when first player coops, second defects
            {{8, 5}, // payoffs when first player defects, second coops
                    {5, 2}}  // payoffs when first and second players defect
    };

    /*
     * So payoff[i][j][k] represents the payoff to player 1 when the first player's
     * action is i, the second player's action is j, and the third player's action
     * is k.
     *
     * In this simulation, triples of players will play each other repeatedly in a
     * 'match'. A match consists of about 100 rounds, and your score from that match
     * is the average of the payoffs from each round of that match. For each round,
     * your strategy is given a list of the previous plays (so you can remember what
     * your opponent did) and must compute the next action.
     */

    abstract class Player {
        // This procedure takes in the number of rounds elapsed so far (n), and
        // the previous plays in the match, and returns the appropriate action.
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            throw new RuntimeException("You need to override the selectAction method.");
        }

        // Used to extract the name of this player class.
        final String name() {
            String result = getClass().getName();
            return result.substring(result.indexOf('$') + 1);
        }
    }

    /* Here are four simple strategies: */

    class NicePlayer extends Player {
        // NicePlayer always cooperates
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            return 0;
        }
    }

    class NastyPlayer extends Player {
        // NastyPlayer always defects
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            return 1;
        }
    }

    class RandomPlayer extends Player {
        // RandomPlayer randomly picks his action each time
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            if (Math.random() < 0.5)
                return 0; // cooperates half the time
            else
                return 1; // defects half the time
        }
    }

    class TolerantPlayer extends Player {
        // TolerantPlayer looks at his opponents' histories, and only defects
        // if at least half of the other players' actions have been defects
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            int opponentCoop = 0;
            int opponentDefect = 0;
            for (int i = 0; i < n; i++) {
                if (oppHistory1[i] == 0)
                    opponentCoop = opponentCoop + 1;
                else
                    opponentDefect = opponentDefect + 1;
            }
            for (int i = 0; i < n; i++) {
                if (oppHistory2[i] == 0)
                    opponentCoop = opponentCoop + 1;
                else
                    opponentDefect = opponentDefect + 1;
            }
            if (opponentDefect > opponentCoop)
                return 1;
            else
                return 0;
        }
    }

    class FreakyPlayer extends Player {
        // FreakyPlayer determines, at the start of the match,
        // either to always be nice or always be nasty.
        // Note that this class has a non-trivial constructor.
        int action;

        FreakyPlayer() {
            if (Math.random() < 0.5)
                action = 0; // cooperates half the time
            else
                action = 1; // defects half the time
        }

        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            return action;
        }
    }

    class T4TPlayer extends Player {
        // Picks a random opponent at each play,
        // and uses the 'tit-for-tat' strategy against them
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            if (n == 0)
                return 0; // cooperate by default
            if (oppHistory1[n-1] == 1 || oppHistory2[n-1] == 1) {
                if (myHistory[n-1] == 0) return 1;
            }
            return 0;
        }
    }

    /**
     * Switch choice if unsure
     */
    class T4TSwitchPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // cooperate by default
            if (n == 0)
                return 0;

            // https://www.sciencedirect.com/science/article/abs/pii/S0096300316301011
            if (oppHistory1[n - 1] == oppHistory2[n - 1])
                return oppHistory1[n - 1];

            return (myHistory[n - 1] == 0) ? 1 : 0;
        }
    }

    /**
     * Stay with using the previous choice if unsure.
     */
    class T4TStayPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // cooperate by default
            if (n == 0)
                return 0;

            // https://www.sciencedirect.com/science/article/abs/pii/S0096300316301011
            if (oppHistory1[n - 1] == oppHistory2[n - 1])
                return oppHistory1[n - 1];

            return myHistory[n - 1];
        }
    }

    /**
     * Cooperates if unsure.
     */
    class T4TCoopPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // cooperate by default
            if (n == 0)
                return 0;

            // https://www.sciencedirect.com/science/article/abs/pii/S0096300316301011
            if (oppHistory1[n - 1] == oppHistory2[n - 1])
                return oppHistory1[n - 1];

            return 0;
        }
    }

    /**
     * Defects if unsure.
     */
    class T4TDefectPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // cooperate by default
            if (n == 0)
                return 0;

            // https://www.sciencedirect.com/science/article/abs/pii/S0096300316301011
            if (oppHistory1[n - 1] == oppHistory2[n - 1])
                return oppHistory1[n - 1];

            return 1;
        }
    }

    /**
     * Compares player history, then cooperates if my defection rate is >= others; else defect
     */
    class HistoryPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            int myNumDefections = 0;
            int oppNumDefections1 = 0;
            int oppNumDefections2 = 0;

            for (int index = 0; index < n; ++index) {
                myNumDefections += myHistory[index];
                oppNumDefections1 += oppHistory1[index];
                oppNumDefections2 += oppHistory2[index];
            }

            if (myNumDefections >= oppNumDefections1 && myNumDefections >= oppNumDefections2)
                return 0;
            else
                return 1;
        }
    }

    /**
     * Less-tolerant than TolerantPlayer
     */
    class LessTolerantPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // cooperate by default
            if (n == 0)
                return 0;

            // TolerantPlayer
            int opponentCoop = 0;
            int opponentDefect = 0;

            for (int i = 0; i < n; i++) {
                if (oppHistory1[i] == 0)
                    opponentCoop += 1;
                else
                    opponentDefect += 1;

                if (oppHistory2[i] == 0)
                    opponentCoop += 1;
                else
                    opponentDefect += 1;
            }

            return (opponentDefect >= opponentCoop) ? 1 : 0;
        }
    }

    /**
     * Always switches choice.
     */
    class SwitchPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // cooperate by default
            if (n == 0)
                return 0;

            return (myHistory[n - 1] == 0) ? 1 : 0;
        }
    }

    /**
     * Combination of T4T and Tolerant
     */
    class T4TTolerantPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // cooperate by default
            if (n == 0)
                return 0;

            // https://www.sciencedirect.com/science/article/abs/pii/S0096300316301011
            if (oppHistory1[n - 1] == oppHistory2[n - 1])
                return oppHistory1[n - 1];

            // TolerantPlayer
            int opponentCoop = 0;
            int opponentDefect = 0;

            for (int i = 0; i < n; i++) {
                if (oppHistory1[i] == 0)
                    opponentCoop += 1;
                else
                    opponentDefect += 1;

                if (oppHistory2[i] == 0)
                    opponentCoop += 1;
                else
                    opponentDefect += 1;
            }

            return (opponentDefect > opponentCoop) ? 1 : 0;
        }
    }

    /**
     * Combination of T4T and LessTolerant
     */
    class T4TLessTolerantPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // cooperate by default
            if (n == 0)
                return 0;

            // https://www.sciencedirect.com/science/article/abs/pii/S0096300316301011
            if (oppHistory1[n - 1] == oppHistory2[n - 1])
                return oppHistory1[n - 1];

            // TolerantPlayer
            int opponentCoop = 0;
            int opponentDefect = 0;

            for (int i = 0; i < n; i++) {
                if (oppHistory1[i] == 0)
                    opponentCoop += 1;
                else
                    opponentDefect += 1;

                if (oppHistory2[i] == 0)
                    opponentCoop += 1;
                else
                    opponentDefect += 1;
            }

            return (opponentDefect >= opponentCoop) ? 1 : 0;
        }
    }

    /**
     * Combination of T4T and History
     */
    class T4THistoryPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // cooperate by default
            if (n == 0)
                return 0;

            // https://www.sciencedirect.com/science/article/abs/pii/S0096300316301011
            if (oppHistory1[n - 1] == oppHistory2[n - 1])
                return oppHistory1[n - 1];

            // HistoryPlayer
            int myNumDefections = 0;
            int oppNumDefections1 = 0;
            int oppNumDefections2 = 0;

            for (int index = 0; index < n; ++index) {
                myNumDefections += myHistory[index];
                oppNumDefections1 += oppHistory1[index];
                oppNumDefections2 += oppHistory2[index];
            }

            if (myNumDefections >= oppNumDefections1 && myNumDefections >= oppNumDefections2)
                return 0;
            else
                return 1;
        }
    }

    /**
     * Combination of T4T, Tolerant and History
     */
    class T4TTolerantHistoryPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            if (n == 0)
                return 0; // cooperate by default

            if (n >= 109)
                return 1; // opponents cannot retaliate

            // https://www.sciencedirect.com/science/article/abs/pii/S0096300316301011
            if (oppHistory1[n - 1] == oppHistory2[n - 1])
                return oppHistory1[n - 1];

            // n starts at 0, so compare history first
            if (n % 2 != 0) { // odd round - be tolerant
                // TolerantPlayer
                int opponentCoop = 0;
                int opponentDefect = 0;

                for (int i = 0; i < n; i++) {
                    if (oppHistory1[i] == 0)
                        opponentCoop += 1;
                    else
                        opponentDefect += 1;

                    if (oppHistory2[i] == 0)
                        opponentCoop += 1;
                    else
                        opponentDefect += 1;
                }

                return (opponentDefect > opponentCoop) ? 1 : 0;
            }
            // else: even round - compare history

            // HistoryPlayer
            int myNumDefections = 0;
            int oppNumDefections1 = 0;
            int oppNumDefections2 = 0;

            for (int index = 0; index < n; ++index) {
                myNumDefections += myHistory[index];
                oppNumDefections1 += oppHistory1[index];
                oppNumDefections2 += oppHistory2[index];
            }

            if (myNumDefections >= oppNumDefections1 && myNumDefections >= oppNumDefections2)
                return 0;
            else
                return 1;
        }
    }

    /**
     * Combination of T4T and Tolerant; also tries to take advantage of NicePlayer
     */
    class T4TTolerantTakeAdvantagePlayer extends Player {
        private int numRoundsThreshold = 10;

        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // cooperate by default
            if (n == 0)
                return 0;

            if (n >= numRoundsThreshold) {
                int iDefect = 0;
                int oppDefect1 = 0;
                int oppDefect2 = 0;

                for (int index = n - 1; index > n - 1 - numRoundsThreshold; --index) {
                    iDefect += myHistory[index];
                    oppDefect1 += oppHistory1[index];
                    oppDefect2 += oppHistory2[index];
                }

                if (iDefect == 0 && oppDefect1 == 0 && oppDefect2 == 0)
                    return 1; // take advantage
            }

            // Performance becomes worse when trying to punish others
            // for punish self, for ownself defecting when taking advantage.

            // if (oppHistory1[n-1] == oppHistory2[n-1])
            // 	return oppHistory1[n-1];

            // Use modified tit-for-tat instead.

            if (oppHistory1[n - 1] == 0 && oppHistory2[n - 1] == 0)
                return 0; // cooperate along

            if (oppHistory1[n - 1] == 1 && oppHistory2[n - 1] == 1 && myHistory[n - 1] != 1)
                return 1; // both defect while i cooperate

            // TolerantPlayer
            int opponentCoop = 0;
            int opponentDefect = 0;

            for (int i = 0; i < n; i++) {
                if (oppHistory1[i] == 0)
                    opponentCoop += 1;
                else
                    opponentDefect += 1;

                if (oppHistory2[i] == 0)
                    opponentCoop += 1;
                else
                    opponentDefect += 1;
            }

            return (opponentDefect > opponentCoop) ? 1 : 0;
        }
    }

    class StrategicPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {

            // First move: cooperate
            if (n == 0) return 0;

            // End game strategy: Defect in the last rounds when opponents can't retaliate
            if (n >= 104) return 1;

            // Calculate the recent defection rates over the last 10 rounds or less
            double defRate1 = calculateDefectionRate(oppHistory1, Math.min(10, n));
            double defRate2 = calculateDefectionRate(oppHistory2, Math.min(10, n));

            // Adapt strategy based on recent defection rates
            if (defRate1 > 0.7 || defRate2 > 0.7) {
                // Defect if either opponent has a high recent defection rate
                return 1;
            } else if (defRate1 < 0.3 && defRate2 < 0.3) {
                // Cooperate if both opponents have low defection rates
                return 0;
            } else {
                // Otherwise, use a modified tit-for-tat strategy
                return oppHistory1[n - 1] == oppHistory2[n - 1] ? oppHistory1[n - 1] : 1;
            }
        }

        // Helper method to calculate the defection rate
        private double calculateDefectionRate(int[] oppHistory, int n) {
            int defections = 0;
            for (int i = 0; i < n; i++) {
                if (oppHistory[i] == 1) defections++;
            }
            return defections / (double) n;
        }
    }

    class Ngo_Jason_Player extends Player { // extends Player
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            if (n == 0)
                return 0; // cooperate by default

            if (n >= 109)
                return 1; // opponents cannot retaliate

            // https://www.sciencedirect.com/science/article/abs/pii/S0096300316301011
            if (oppHistory1[n - 1] == oppHistory2[n - 1])
                return oppHistory1[n - 1];

            // n starts at 0, so compare history first

            if (n % 2 != 0) { // odd round - be tolerant
                // TolerantPlayer
                int opponentCoop = 0;
                int opponentDefect = 0;

                for (int i = 0; i < n; i++) {
                    if (oppHistory1[i] == 0)
                        opponentCoop += 1;
                    else
                        opponentDefect += 1;

                    if (oppHistory2[i] == 0)
                        opponentCoop += 1;
                    else
                        opponentDefect += 1;
                }

                return (opponentDefect > opponentCoop) ? 1 : 0;
            }
            // else: even round - compare history

            // HistoryPlayer
            int myNumDefections = 0;
            int oppNumDefections1 = 0;
            int oppNumDefections2 = 0;

            for (int index = 0; index < n; ++index) {
                myNumDefections += myHistory[index];
                oppNumDefections1 += oppHistory1[index];
                oppNumDefections2 += oppHistory2[index];
            }

            if (myNumDefections >= oppNumDefections1 && myNumDefections >= oppNumDefections2)
                return 0;
            else
                return 1;
        }
    }

    class Naing_Htet_Player extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {

            // Rule 1: our agent will cooperate in the first round
            if (n == 0) {
                return 0;
            }

            // Rule 2: our agent will defect in the last few rounds, NastyPlayer mode is turned on
            if (n > 95) {
                return 1;
            }

            // Rule 3: if all players including our agent cooperated in the previous round,
            // then our agent will continue to cooperate
            if (myHistory[n - 1] == 0 && oppHistory1[n - 1] == 0 && oppHistory2[n - 1] == 0) {
                return 0;
            }

            // Rule 4: check opponents history to see if they have defected before
            for (int i = 0; i < n; i++) {
                if (oppHistory1[i] == 1 || oppHistory2[i] == 1) {
                    // if either one of them defected before, our agent will always defect
                    return 1;
                }
            }
            // Rule 5: Otherwise, by default nature, our agent will always cooperate
            return 0;
        }
    }

    class WILSON_TENG_Player extends Player {
        final String NAME = "[██] WILSON_THURMAN_TENG";
        final String MATRIC_NO = "[██] U1820540H";

        int[][][] payoff = {
                {{6, 3},     //payoffs when first and second players cooperate
                        {3, 0}},     //payoffs when first player coops, second defects
                {{8, 5},     //payoffs when first player defects, second coops
                        {5, 2}}};    //payoffs when first and second players defect

        int r;
        int[] myHist, opp1Hist, opp2Hist;
        int myScore = 0, opp1Score = 0, opp2Score = 0;
        int opponent1Coop = 0;
        int opponent2Coop = 0;

        final double LENIENT_THRESHOLD = 0.705; // Used for Law [#1]
        final double STRICT_THRESHOLD = 0.750; // Used for Law [#2]

        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            /**
             LAWS:
             [#0] Unless I am losing, be trustworthy and unpredictable at the same time.
             [#1] Protect myself.
             [#2] Cooperate in a cooperative environment.
             [#3] If I am losing, turn it into a lose-lose situation.
             */

            // Assume environment is cooperative. Always cooperate in first round!
            if (n == 0) return 0;

            // Updating class variables for use in methods.
            this.r = n - 1; // previous round index
            this.myHist = myHistory;
            this.opp1Hist = oppHistory1;
            this.opp2Hist = oppHistory2;

            // Updating Last Actions (LA) for all players.
            int myLA = myHistory[r];
            int opp1LA = oppHistory1[r];
            int opp2LA = oppHistory2[r];

            // Updating Scores for all players
            this.myScore += payoff[myLA][opp1LA][opp2LA];
            this.opp1Score += payoff[opp1LA][opp2LA][myLA];
            this.opp2Score += payoff[opp2LA][opp1LA][myLA];

            // Update opponent's cooperate record.
            if (n > 0) {
                opponent1Coop += oppAction(opp1Hist[r]);
                opponent2Coop += oppAction(opp2Hist[r]);
            }
            // Calculate opponent's cooperate probability.
            double opponent1Coop_prob = opponent1Coop / opp1Hist.length;
            double opponent2Coop_prob = opponent2Coop / opp2Hist.length;

            /** [PROTECT MYSELF]: -> Law [#1]
             When it is nearing the end of the tournament at 100 rounds, if both players are known to be relatively nasty
             (cooperate less than 75% of the time). Defect to protect myself.
             */
            if ((n > 100) && (opponent1Coop_prob < STRICT_THRESHOLD && opponent2Coop_prob < STRICT_THRESHOLD)) {
                // Law [#0] Added
                return actionWithNoise(1, 99);
            }

            /** [REWARD COOPERATION]: -> Law [#2]
             At any point in time before we are able to accurately decide if opponents are nasty or not. We set a lenient
             threshold (0.705) to gauge if opponents are cooperative. Additionally, we check if both opponent's last action
             was to cooperate. If yes, we will cooperate too.
             */
            if ((opp1LA + opp2LA == 0) && (opponent1Coop_prob > LENIENT_THRESHOLD && opponent2Coop_prob > LENIENT_THRESHOLD)) {
                // Law [#0] Added
                return actionWithNoise(0, 99);
            } else
            /** [I WILL NOT LOSE] -> Law [#3]
             However, if opponent is not cooperative, we will check if we have the highest score.
             If we have the highest score, we are appeased and will cooperate. Else, we will defect.
             */
                return SoreLoser();
        }

        /**
         * Law [#0]: This utility method introduces noise to an agent's action, allowing it to be unpredictable.
         *
         * @param intendedAction                     The agent's intended action.
         * @param percent_chance_for_intended_action The percentage chance the agent will perform it's intended action.
         * @return The agent's final action.
         */
        private int actionWithNoise(int intendedAction, int percent_chance_for_intended_action) {
            Map<Integer, Integer> map = new HashMap<Integer, Integer>() {{
                put(intendedAction, percent_chance_for_intended_action);
                put(oppAction(intendedAction), 1 - percent_chance_for_intended_action);
            }};
            LinkedList<Integer> list = new LinkedList<>();
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                for (int i = 0; i < entry.getValue(); i++) {
                    list.add(entry.getKey());
                }
            }
            Collections.shuffle(list);
            return list.pop();
        }

        /**
         * Law [#3]:
         * Cooperates if agent currently has the highest score, else defect.
         *
         * @return
         */
        private int SoreLoser() {
            if (iAmWinner()) return 0;
            return 1;
        }

        /* Function to check if agent is loser or not. Agent is a winner if it has the highest score. */
        private boolean iAmWinner() {
            if (myScore >= opp1Score && myScore >= opp2Score) {
                return true;
            }
            return false;
        }

        /* Utility method to obtain opposite action. */
        private int oppAction(int action) {
            if (action == 1) return 0;
            return 1;
        }
    }

    class Huang_KyleJunyuan_Player extends Player {
        // Helper function to calculate percentage of cooperation
        float calCoopPercentage(int[] history) {
            int cooperates = 0;
            int length = history.length;

            for (int i = 0; i < length; i++)
                if (history[i] == 0)
                    cooperates++;

            return (float) cooperates / length * 100;
        }

        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            if (n == 0)
                return 0; // First round: Cooperate

            /* 1. Calculate percentage of cooperation */
            float perOpp1Coop = calCoopPercentage(oppHistory1);
            float perOpp2Coop = calCoopPercentage(oppHistory2);

            /* 2. If both players are mostly cooperating */
            if (perOpp1Coop > 90 && perOpp2Coop > 90) {
                int range = (10 - 5) + 1; // Max: 10, Min: 5
                int random = (int) (Math.random() * range) + 5;

                if (n > (90 + random))  // Selfish: Last min defect
                    return 1;
                else
                    return 0;    // First ~90 rounds: Cooperate
            }

            /* 3. Defect by default */
            return 1;
        }
    }

    class ImprovedStrategicPlayer extends Player {
        private int[] myHistory;
        private int[] oppHistory1;
        private int[] oppHistory2;

        // Constructor
        ImprovedStrategicPlayer() {
            myHistory = new int[0];
            oppHistory1 = new int[0];
            oppHistory2 = new int[0];
        }

        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            this.myHistory = myHistory;
            this.oppHistory1 = oppHistory1;
            this.oppHistory2 = oppHistory2;

            // First move: always cooperate
            if (n == 0) return 0;

            // End game strategy: defect in the last rounds when opponents can't retaliate
            if (n >= 104) return 1;

            // Calculate the recent defection rates over the last 10 rounds
            double defRate1 = calculateDefectionRate(oppHistory1, Math.min(10, n));
            double defRate2 = calculateDefectionRate(oppHistory2, Math.min(10, n));

            // If either opponent has a high recent defection rate, defect
            if (defRate1 > 0.7 || defRate2 > 0.7) {
                return 1;
            }

            // If both opponents have a low defection rate, cooperate
            if (defRate1 < 0.3 && defRate2 < 0.3) {
                return 0;
            }

            // Otherwise, employ a standard Tit-for-Tat strategy
            if (oppHistory1[n - 1] == oppHistory2[n - 1]) {
                return oppHistory1[n - 1];
            }

            // If there is no consensus, defect to safeguard against potential defection
            return 1;
        }

        private double calculateDefectionRate(int[] history, int length) {
            int defections = 0;
            for (int i = history.length - length; i < history.length; i++) {
                if (history[i] == 1) {
                    defections++;
                }
            }
            return defections / (double) length;
        }
    }

    class EnhancedPlayer extends Player {
        int r;
        int[] myHist, opp1Hist, opp2Hist;
        int myScore = 0, opp1Score = 0, opp2Score = 0;
        int opponent1Coop = 0;
        int opponent2Coop = 0;

        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            if (n == 0) return 0;  // Always cooperate in the first round

            // Update class variables
            updateScoresAndHistories(n, myHistory, oppHistory1, oppHistory2);

            // Calculate cooperation probabilities
            double opp1CoopProb = (double) opponent1Coop / n;
            double opp2CoopProb = (double) opponent2Coop / n;

            // Determine action based on the game stage and opponent behavior
            if (n > 104) {  // Enter endgame strategy earlier
                if (opp1CoopProb < 0.5 || opp2CoopProb < 0.5) {
                    return 1;  // Defect if any opponent is less cooperative
                }
            }

            // Respond based on the combined last action of opponents
            if (opp1Hist[n - 1] == 0 && opp2Hist[n - 1] == 0) {
                return 0;  // Cooperate if both opponents cooperated last round
            }

            return 1;  // Default to defection if any uncertainty remains
        }

        void updateScoresAndHistories(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            this.r = n - 1;
            this.myHist = myHistory;
            this.opp1Hist = oppHistory1;
            this.opp2Hist = oppHistory2;
            updateScores();
            updateCooperationCounts();
        }

        void updateScores() {
            myScore += payoff[myHist[r]][opp1Hist[r]][opp2Hist[r]];
            opp1Score += payoff[opp1Hist[r]][opp2Hist[r]][myHist[r]];
            opp2Score += payoff[opp2Hist[r]][opp1Hist[r]][myHist[r]];
        }

        void updateCooperationCounts() {
            opponent1Coop += opp1Hist[r] == 0 ? 1 : 0;
            opponent2Coop += opp2Hist[r] == 0 ? 1 : 0;
        }
    }

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


    /*
     * In our tournament, each pair of strategies will play one match against each
     * other. This procedure simulates a single match and returns the scores.
     */
    float[] scoresOfMatch(Player A, Player B, Player C, int rounds) {
        int[] HistoryA = new int[0], HistoryB = new int[0], HistoryC = new int[0];
        float ScoreA = 0, ScoreB = 0, ScoreC = 0;

        for (int i = 0; i < rounds; i++) {
            int PlayA = A.selectAction(i, HistoryA, HistoryB, HistoryC);
            int PlayB = B.selectAction(i, HistoryB, HistoryC, HistoryA);
            int PlayC = C.selectAction(i, HistoryC, HistoryA, HistoryB);
            ScoreA = ScoreA + payoff[PlayA][PlayB][PlayC];
            ScoreB = ScoreB + payoff[PlayB][PlayC][PlayA];
            ScoreC = ScoreC + payoff[PlayC][PlayA][PlayB];
            HistoryA = extendIntArray(HistoryA, PlayA);
            HistoryB = extendIntArray(HistoryB, PlayB);
            HistoryC = extendIntArray(HistoryC, PlayC);
        }
        float[] result = {ScoreA / rounds, ScoreB / rounds, ScoreC / rounds};
        return result;
    }

    // This is a helper function needed by scoresOfMatch.
    int[] extendIntArray(int[] arr, int next) {
        int[] result = new int[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        result[result.length - 1] = next;
        return result;
    }

    /*
     * The procedure makePlayer is used to reset each of the Players (strategies) in
     * between matches. When you add your own strategy, you will need to add a new
     * entry to makePlayer, and change numPlayers.
     */

    int numPlayers = 5; // includes custom Players

    Player makePlayer(int which) {
        switch (which) {
            case 0:
                return new Ngo_Jason_Player();
            case 1:
                return new WILSON_TENG_Player();
            case 2:
                return new Cholakov_Kristiyan_Kamenov_Player();
            case 3:
                return new Naing_Htet_Player();
            case 4:
                 return new Huang_KyleJunyuan_Player();
        }
        throw new RuntimeException("Bad argument passed to makePlayer");
    }

    /* Finally, the remaining code actually runs the tournament. */

    public static void main(String[] args) {
        CustomThreePrisonersDilemma instance = new CustomThreePrisonersDilemma();
        instance.runTournament();

        // Run the tournament 1000 times and get the average score of each player and the average position of the player
        int numTournaments = 1000;
        float[] totalScore = new float[instance.numPlayers];
        float[] totalPosition = new float[instance.numPlayers];
        int[] firstPlaceCount = new int[instance.numPlayers]; // Array to track first place finishes

        for (int i = 0; i < numTournaments; i++) {
            // Define new tournament instance
            instance = new CustomThreePrisonersDilemma();
            float[] score = instance.runTournamentOnce();
            int[] sortedOrder = new int[instance.numPlayers];
            // Initialize the sorted order array
            for (int j = 0; j < instance.numPlayers; j++) {
                sortedOrder[j] = j;
            }
            // Sort players by score using insertion sort
            for (int j = 1; j < instance.numPlayers; j++) {
                float currentScore = score[j];
                int currentId = sortedOrder[j];
                int k = j - 1;
                while (k >= 0 && score[sortedOrder[k]] < currentScore) {
                    sortedOrder[k + 1] = sortedOrder[k];
                    k--;
                }
                sortedOrder[k + 1] = currentId;
            }
            // Accumulate scores and positions
            for (int j = 0; j < instance.numPlayers; j++) {
                totalScore[sortedOrder[j]] += score[sortedOrder[j]];
                totalPosition[sortedOrder[j]] += j;
            }
            // Increment first place count for the player with the highest score
            firstPlaceCount[sortedOrder[0]]++;
        }

        System.out.println("\nAverage score of each player after " + numTournaments + " tournaments:");
        for (int i = 0; i < instance.numPlayers; i++) {
            System.out.println(instance.makePlayer(i).name() + ": " + (totalScore[i] / numTournaments));
        }

        System.out.println("\nAverage position of each player after " + numTournaments + " tournaments:");
        for (int i = 0; i < instance.numPlayers; i++) {
            System.out.println(instance.makePlayer(i).name() + ": " + (totalPosition[i] / numTournaments + 1) );
        }

        System.out.println("\nNumber of times each player finished in 1st place:");
        for (int i = 0; i < instance.numPlayers; i++) {
            System.out.println(instance.makePlayer(i).name() + ": " + firstPlaceCount[i]);
        }
    }

    boolean verbose = false; // set verbose = false if you get too much text output

    float[] runTournamentOnce() {
        float[] totalScore = new float[numPlayers];

        // This loop plays each triple of players against each other.
        // Note that we include duplicates: two copies of your strategy will play once
        // against each other strategy, and three copies of your strategy will play
        // once.

        for (int i = 0; i < numPlayers; i++)
            for (int j = i; j < numPlayers; j++)
                for (int k = j; k < numPlayers; k++) {

                    Player A = makePlayer(i); // Create a fresh copy of each player
                    Player B = makePlayer(j);
                    Player C = makePlayer(k);
                    int rounds = 90 + (int) Math.rint(20 * Math.random()); // Between 90 and 110 rounds
                    float[] matchResults = scoresOfMatch(A, B, C, rounds); // Run match
                    totalScore[i] = totalScore[i] + matchResults[0];
                    totalScore[j] = totalScore[j] + matchResults[1];
                    totalScore[k] = totalScore[k] + matchResults[2];
                    if (verbose)
                        System.out.println(A.name() + " scored " + matchResults[0] + " points, " + B.name() + " scored "
                                + matchResults[1] + " points, and " + C.name() + " scored " + matchResults[2]
                                + " points.");
                }
        return totalScore;
    }

    void runTournament() {
        float[] totalScore = new float[numPlayers];

        // This loop plays each triple of players against each other.
        // Note that we include duplicates: two copies of your strategy will play once
        // against each other strategy, and three copies of your strategy will play
        // once.

        for (int i = 0; i < numPlayers; i++)
            for (int j = i; j < numPlayers; j++)
                for (int k = j; k < numPlayers; k++) {

                    Player A = makePlayer(i); // Create a fresh copy of each player
                    Player B = makePlayer(j);
                    Player C = makePlayer(k);
                    int rounds = 90 + (int) Math.rint(20 * Math.random()); // Between 90 and 110 rounds
                    float[] matchResults = scoresOfMatch(A, B, C, rounds); // Run match
                    totalScore[i] = totalScore[i] + matchResults[0];
                    totalScore[j] = totalScore[j] + matchResults[1];
                    totalScore[k] = totalScore[k] + matchResults[2];
                    if (verbose)
                        System.out.println(A.name() + " scored " + matchResults[0] + " points, " + B.name() + " scored "
                                + matchResults[1] + " points, and " + C.name() + " scored " + matchResults[2]
                                + " points.");
                }
        int[] sortedOrder = new int[numPlayers];
        // This loop sorts the players by their score.
        for (int i = 0; i < numPlayers; i++) {
            int j = i - 1;
            for (; j >= 0; j--) {
                if (totalScore[i] > totalScore[sortedOrder[j]])
                    sortedOrder[j + 1] = sortedOrder[j];
                else
                    break;
            }
            sortedOrder[j + 1] = i;
        }

        // Finally, print out the sorted results.
        if (verbose)
            System.out.println();
        System.out.println("Tournament Results");
        for (int i = 0; i < numPlayers; i++)
            System.out.println(makePlayer(sortedOrder[i]).name() + ": " + totalScore[sortedOrder[i]] + " points.");

    } // end of runTournament()

} // end of class PrisonersDilemma