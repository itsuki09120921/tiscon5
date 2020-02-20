package com.tiscon.controller;

import com.tiscon.dao.EstimateDao;
import com.tiscon.dto.UserOrderDto;
import com.tiscon.form.UserOrderForm;
import com.tiscon.form.UserOrderForm2;
import com.tiscon.service.EstimateService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 引越し見積もりのコントローラークラス。
 *
 * @author Oikawa Yumi
 */
@Controller
public class EstimateController {

    private final EstimateDao estimateDAO;

    private final EstimateService estimateService;

    /**
     * コンストラクタ
     *
     * @param estimateDAO EstimateDaoクラス
     * @param estimateService EstimateServiceクラス
     */
    public EstimateController(EstimateDao estimateDAO, EstimateService estimateService) {
        this.estimateDAO = estimateDAO;
        this.estimateService = estimateService;
    }

    /** (1)Top画面*/
    @GetMapping("")
    String index(Model model) {
        return "top";
    }

    /**
     * （２）見積もり必要項目入力画面に遷移する。
     *
     * @param model 遷移先に連携するデータ
     * @return 遷移先
     */
    @GetMapping("input2")
    String input2(Model model) {
        if (!model.containsAttribute("userOrderForm")) {
            model.addAttribute("userOrderForm", new UserOrderForm());
        }

        model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
        return "input2";
    }
    /**@GetMapping("input")
    String input(Model model) {
        if (!model.containsAttribute("userOrderForm")) {
            model.addAttribute("userOrderForm", new UserOrderForm());
        }

        model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
        return "input";
    }

    /**
     * TOP画面に戻る。
     *
     * @param model 遷移先に連携するデータ
     * @return 遷移先
     */
    @PostMapping(value = "", params = "backToTop")
    String backToTop(Model model) {
        return "top";
    }

    /**
     * 確認画面に遷移する。
     *
     * @param userOrderForm 顧客が入力した見積もり依頼情報
     * @param model         遷移先に連携するデータ
     * @return 遷移先
     */
    @PostMapping(value = "submit", params = "confirm")
    String confirm(@Validated UserOrderForm userOrderForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
            model.addAttribute("userOrderForm", userOrderForm);
            return "input2";
        }

        model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
        model.addAttribute("userOrderForm", userOrderForm);
        return "result1";
    }

    /**
     * (2)の入力画面に戻る。
     *
     * @param userOrderForm 顧客が入力した見積もり依頼情報
     * @param model         遷移先に連携するデータ
     * @return 遷移先
     */
    @PostMapping(value = "result1", params = "backToInput")
    String backToInput(UserOrderForm userOrderForm, Model model) {
        model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
        model.addAttribute("userOrderForm", userOrderForm);
        return "input2";
    }

    /**
     * (5)の入力画面に戻る。
     *
     * @param userOrderForm2 顧客が入力した見積もり依頼情報
     * @param model         遷移先に連携するデータ
     * @return 遷移先
     */
    @PostMapping(value = "comfirm1", params = "backToInput")
    String backToInput3(UserOrderForm2 userOrderForm2, Model model) {
        model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
        model.addAttribute("userOrderForm", userOrderForm2);
        return "input3";
    }

    /**
     * 確認画面に戻る。
     *
     * @param userOrderForm 顧客が入力した見積もり依頼情報
     * @param model         遷移先に連携するデータ
     * @return 遷移先
     */
    @PostMapping(value = "order", params = "backToConfirm")
    String backToConfirm(UserOrderForm userOrderForm, Model model) {
        model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
        model.addAttribute("userOrderForm", userOrderForm);
        return "confirm";
    }

    /**
     * 概算見積もり画面に遷移する。
     *
     * @param userOrderForm 顧客が入力した見積もり依頼情報

     * @param model         遷移先に連携するデータ
     * @return 遷移先
     */
    @PostMapping(value = "result1", params = "calculation")
    String calculation(@Validated UserOrderForm userOrderForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
            model.addAttribute("userOrderForm", userOrderForm);
            return "input2";
        }
        //料金の計算を行う。
        UserOrderDto dto = new UserOrderDto();
        BeanUtils.copyProperties(userOrderForm, dto);
        Integer price = estimateService.getPrice(dto);

        model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
        model.addAttribute("userOrderForm", userOrderForm);
        model.addAttribute("price", price);
        return "result1";
    }

    /**
     * （5）個人情報入力画面に遷移する。
     *
     * @param model 遷移先に連携するデータ
     * @return 遷移先
     */

    @PostMapping(value = "input3", params = "write")
    String input3(UserOrderForm userOrderForm, UserOrderForm2 userOrderForm2, Model model) {
        //userOrderFormからuserOrderForm2へ値を渡す
        BeanUtils.copyProperties(userOrderForm, userOrderForm2);
        if (!model.containsAttribute("userOrderForm2")) {
            model.addAttribute("userOrderForm2", new UserOrderForm2());
        }

        model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
        return "input3";
    }

    /**
     * （6）確認画面に遷移する。
     *
     * @param model 遷移先に連携するデータ
     * @return 遷移先
     */

    @PostMapping(value = "confirm1", params = "verify")
    String confirm1(@Validated UserOrderForm2 userOrderForm2, BindingResult result, Model model) {

//        //userOrderFormからuserOrderForm2へ値を渡す
//        BeanUtils.copyProperties(userOrderForm, userOrderForm2);
        if (result.hasErrors()) {
            model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
            model.addAttribute("userOrderForm2", userOrderForm2);
            return "input3";
        }
        model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
        model.addAttribute("userOrderForm2", userOrderForm2);
        return "confirm1";
    }


//    /**
//     * (6)の確認画面に戻る。
//     *
//     * @param userOrderForm 顧客が入力した見積もり依頼情報
//     * @param model         遷移先に連携するデータ
//     * @return 遷移先
//     */
//    @PostMapping(value = "order", params = "backToConfirm")
//    String backToConfirm1(UserOrderForm userOrderForm, UserOrderForm2 userOrderForm2, Model model) {
//        //userOrderFormからuserOrderForm2へ値を渡す
//        BeanUtils.copyProperties(userOrderForm, userOrderForm2);
//        model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
//        model.addAttribute("userOrderForm", userOrderForm);
//        return "confirm1";
//    }

    /**
     * (7)申し込み完了画面に遷移する。
     *
     * @param userOrderForm2 顧客が入力した見積もり依頼情報
     * @param result        精査結果
     * @param model         遷移先に連携するデータ
     * @return 遷移先
     */
    @PostMapping(value = "last", params = "complete")
    String complete(@Validated UserOrderForm2 userOrderForm2, BindingResult result, Model model) {
//        if (result.hasErrors()) {
//
//            model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
//            model.addAttribute("userOrderForm2", userOrderForm2);
//            return "confirm1";
//        }

        UserOrderDto dto = new UserOrderDto();
        BeanUtils.copyProperties(userOrderForm2, dto);
        estimateService.registerOrder(dto);

        return "complete";
    }

}
