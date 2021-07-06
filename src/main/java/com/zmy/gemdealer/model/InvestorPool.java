package com.zmy.gemdealer.model;

import lombok.Data;

import java.util.*;

@Data
public class InvestorPool {
    private List<Investor> investors;

    public InvestorPool(int playerNums, List<Investor> allInvestors) {
        switch (playerNums) {
            case 2:
                this.investors = randomPick(allInvestors, 3);
                break;
            case 3:
                this.investors = randomPick(allInvestors, 4);
                break;
            case 4:
                this.investors = randomPick(allInvestors, 5);
                break;
            default:
                throw new RuntimeException("invalid numbers of players");
        }
    }

    public <T> List<T> randomPick(List<T> data, int sum) {
        Set<Integer> resultIndex = new HashSet<>();
        if (resultIndex.size() < sum) {
            int result = (int) (Math.random() * data.size());
            resultIndex.add(result);
        }
        List<T> result = new ArrayList<>(sum);
        resultIndex.forEach(idx -> result.add(data.get(idx)));
        return result;
    }

    public List<Integer> getCanBeInvitedInvestors(Player currentPlayer) {
        List<Integer> result = new LinkedList<>();
        for (int i = 0; i < this.investors.size(); i++) {
            if (this.investors.get(i).canBeInvited(currentPlayer)) {
                result.add(i);
            }
        }
        return result;
    }
}
