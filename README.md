# SC4003 - Repeated Prisoners Dilemma Agent

## Overview

This project implements a competitive agent designed for a three-player repeated Prisoners Dilemma tournament. The solution leverages a hybrid strategy that blends classic game theory approaches with novel dynamic adaptations, resulting in robust performance against a variety of opponents.

## Repository Structure

- **src/**
  - **CustomThreePrisonersDilemma.java**  
    Implements a custom tournament framework for the three-player Prisoners Dilemma. This file contains not only the simulation engine—including match scheduling, score calculations, and sorting of tournament results—but also a wide array of player strategy implementations. Strategies range from basic ones like *NicePlayer* and *NastyPlayer* to advanced, adaptive strategies such as *StrategicPlayer*, *Ngo_Jason_Player*, *WILSON_TENG_Player*, *Huang_KyleJunyuan_Player*, and your custom implementation *Cholakov_Kristiyan_Kamenov_Player*.
    
  - **ThreePrisonersDilemma.java**  
    Acts as the base implementation for the three-player game, setting up the core payoff matrix and providing foundational logic. This file ensures that the three-player dynamics mimic the two-player Prisoners Dilemma when one player's response is fixed and supports symmetric payoff calculations.

- **Cholakov_Kristiyan_Player.java**  
  Contains your dedicated custom agent implementation, which builds upon the tournament framework to deliver a robust strategy that integrates cooperative starts, dynamic history analysis, adaptive endgame tactics, and punitive measures.

- **Final_Report.pdf**  
  A comprehensive report detailing the task, design decisions, implementation process, and performance evaluation of the agent. This document also discusses the novelty and strategic insights of your solution.

## Task Description

The main objective is to design an agent capable of competing in a tournament where each match is played over a random number of rounds (between 90 and 110). The agent must navigate a complex payoff matrix, where cooperation and defection yield different rewards. The tournament environment includes diverse opponent strategies, demanding that the agent adapts dynamically to maximize its average score.

## Novelty of the Solution

Your solution distinguishes itself through several innovative features:

- **Hybrid Strategic Approach:**  
  Integrates classic strategies (e.g., Tit-for-Tat, Tit-for-Two-Tats, Grim Trigger) with dynamic adaptations. This enables the agent to adjust its behavior in real time based on historical data and opponent actions.

- **Cooperative Initiation:**  
  The agent begins every match with cooperation, setting a foundation for mutual benefit and increased opportunities for long-term cooperation.

- **Dynamic History Analysis:**  
  Constantly evaluates opponents’ past actions and cooperation rates, allowing the agent to detect patterns and tailor its strategy accordingly.

- **Adaptive Endgame Tactics:**  
  Recognizes the shift in dynamics during the final rounds, switching to a defection mode when opponents can no longer retaliate, thereby maximizing the payoff in the endgame.

- **Punitive Measures:**  
  Employs a trigger mechanism that detects consistent or intentional defections from opponents. Once activated, this mechanism forces the agent into an unconditional defection mode to deter exploitation.

- **Tournament Framework:**  
  The src folder includes a comprehensive tournament simulator that not only pits various strategies against each other but also performs statistical analysis over multiple tournaments, ensuring that performance evaluations are robust and reliable.

## Performance Evaluation

The agent has been rigorously tested in a simulated tournament environment against both standard and advanced strategies (including *NicePlayer*, *NastyPlayer*, *RandomPlayer*, *TolerantPlayer*, *FreakyPlayer*, and various T4T-based players). Metrics such as average scores and ranking positions over multiple tournaments have shown that your custom agent consistently outperforms many competitors, particularly by effectively balancing cooperation with strategic defection.

## Conclusion

The developed agent successfully addresses the challenges of the repeated Prisoners Dilemma by:
- Establishing a cooperative baseline that encourages mutual cooperation.
- Dynamically adjusting its strategy based on historical performance and opponent behavior.
- Employing a robust endgame tactic alongside punitive measures to secure a competitive advantage.

This balanced and adaptive approach not only maximizes overall scores but also ensures fairness and strategic depth, making it a strong contender in diverse multi-agent environments.

## License

This project is licensed under the MIT License.
