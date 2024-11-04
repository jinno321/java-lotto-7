package lotto.controller;

import lotto.model.LottoManagementSystem;
import lotto.model.LottoResult;
import lotto.model.MyWallet;
import lotto.model.YieldCalculator;
import lotto.util.InputParser;
import lotto.util.Validation;
import lotto.view.LottoView;

import java.util.List;

public class LottoController {
    private final LottoView lottoView;

    private final MyWallet myWallet;
    private final LottoManagementSystem lottoManagementSystem;
    private final LottoResult lottoResult;


    public LottoController(){
        this.lottoView = new LottoView();
        this.lottoResult = new LottoResult();
        this.myWallet = new MyWallet();
        this.lottoManagementSystem = new LottoManagementSystem();
    }

    public void purchase() {
        while (true) {
            try {
                String input = lottoView.purchaseInput();
                Validation.validateInteger(input);
                int money = Integer.parseInt(input);
                Validation.validateMoney(money);

                myWallet.saveMoney(money);
                myWallet.buyLottos();
                lottoView.printPurchase(myWallet.getLottos());
                break;
            } catch (IllegalArgumentException e) {
                lottoView.printError(e.getMessage());
            }
        }
    }

    public void setWinningNumbers(){
        while (true) {
            try {
                String input = lottoView.winningInput();
                List<Integer> numbers = InputParser.winningNumParse(input);
                Validation.validateWinningLength(numbers.size());
                Validation.validateNumbersBoundary(numbers);
                Validation.validateNoDuplicates(numbers);

                String bonusInput = lottoView.bonusInput();
                Validation.validateInteger(bonusInput);
                int bonus = Integer.parseInt(bonusInput);
                Validation.validateOneNumBoundary(bonus);
                Validation.validateNoDuplicatesWithBonusNumber(numbers, bonus);

                lottoManagementSystem.setWinningNumbers(numbers);
                lottoManagementSystem.setBonusNumber(bonus);
                break;
            } catch (IllegalArgumentException e) {
                lottoView.printError(e.getMessage());
            }
        }
    }

    public void printStat(){
        lottoManagementSystem.recordRanks(lottoResult,myWallet);
        lottoView.printStat(lottoResult);
        long totalPrizeAmount = lottoResult.getTotalPrizeAmount();
        myWallet.saveWinnings(totalPrizeAmount);
        double yield = YieldCalculator.calculateYield(myWallet);
        lottoView.printYield(yield);
    }
}
