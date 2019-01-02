package com.edel.mw.order.edelmwReports.mongo.aggregation;

import com.edel.mw.order.edelmwReports.common.EdelMWReportConstants;
import com.edel.mw.order.edelmwReports.common.EdelMWReportHelper;
import com.mongodb.BasicDBObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;

import static com.edel.mw.order.edelmwReports.common.EdelMWReportConstants.DATE_FILTER_FORMAT;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

public class MongoDBAggregationQueryHelper {


    public static final Criteria TRADE_TYPE_CRITERIA = Criteria.where("type").is(EdelMWReportConstants.MongoConstants.TRADE_BOOK_DEFAULT_TYPE);

    public static final Criteria ORDER_TYPE_CRITERIA = Criteria.where("type").is(EdelMWReportConstants.MongoConstants.ORDER_BOOK_DEFAULT_TYPE);

    public static final Criteria FILL_ID_EXISTS_CRITERIA = Criteria.where("data.fID").exists(true).andOperator(Criteria.where("data.fID").ne(""));

    public static Criteria getCurrentDateCriteria(){
        return Criteria.where("data.rcvDt").is(
                EdelMWReportHelper.convertEpochToDate((System.currentTimeMillis()), DATE_FILTER_FORMAT));
    }

    public static Criteria getAMOStatusesCriteria(){
        List<String> openAMOOrderStatusList = new ArrayList<>();
        openAMOOrderStatusList.add("modify after market order req received");
        openAMOOrderStatusList.add("after market order req received");
        openAMOOrderStatusList.add("cancelled after market order req received");

        Criteria orderStatusCriteria = Criteria.where("sts").in(openAMOOrderStatusList);
        return  orderStatusCriteria;
    }

    public static Criteria getOrderNumberCriteria(String orderId){
        return Criteria.where("data.oID").is(orderId);
    }

    public static Criteria getDateinBetweenCriteria(String startDate, String endDate){

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("data.rcvDt").gte(startDate),Criteria.where("data.rcvDt").lte(endDate));
        return criteria;
    }

    public static final AggregationOperation DESCENDING_DATA_EXTORDTIM_SORT_OPERATION =
            sort(Sort.Direction.DESC,"data.extOrdTim");

    public static final AggregationOperation ASCENDING_DATA_EXTORDTIM_SORT_OPERATION =
            sort(Sort.Direction.ASC,"data.extOrdTim");

    public static final AggregationOperation DESCENDING_EXTORDTIM_SORT_OPERATION =
            sort(Sort.Direction.DESC,"extOrdTim");

    public static final AggregationOperation ASCENDING_EXTORDTIM_SORT_OPERATION =
            sort(Sort.Direction.ASC,"extOrdTim");

    public static final AggregationOperation ORDER_LOGS_RESPONSE_GROUP_OPERATION =
            group("data.oID")
                    .first("data.cancel").as("cancel") // not needed
                    .first("data.edit").as("edit")// not needed
                    .first("data.exit").as("exit")// not needed
                    .first("data.edit").as("edit")// not needed

                    .first("data.sym").as("sym") // not changing
                    .first("data.dpName").as("dpName") // not changing
                    .first("data.aTyp").as("aTyp")// not changing
                    .first("data.cName").as("cName")// not changing
                    .first("data.dExpDt").as("dExpDt")// not changing
                    .first("data.dInsTyp").as("dInsTyp")// not changing
                    .first("data.dQty").as("dQty")// not changing
                    .first("data.desc").as("desc")// not changing
                    .first("data.dur").as("dur")// not changing
                    .first("data.desc").as("desc")// not changing
                    .first("data.exc").as("exc") // not changing
                    .first("data.lsz").as("lsz")// not changing
                    .first("data.oID").as("oID")// not changing
                    .first("data.ogt").as("ogt")// not changing
                    .first("data.rcvDt").as("rcvDt")// not changing
                    .first("data.opTyp").as("opTyp")// not changing
                    .first("data.srs").as("srs")// not changing
                    .first("data.stkPrc").as("stkPrc")// not changing
                    .first("data.tSym").as("tSym")// not changing
                    .first("data.tsz").as("tsz")// not changing
                    .first("data.pTyp").as("pTyp") // not changing

                    .push(new BasicDBObject("isCOSecLeg","$data.isCOSecLeg")
                            .append("extOrdTim","$data.extOrdTim")
                            .append("sts","$data.sts")
                            .append("avgPrc", "$data.avgPrc")
                            .append("exONo","$data.exONo")
                            .append("fQty","$data.fQty")
                            .append("nReqID", "$data.nReqID")
                            .append("oTyp","$data.oTyp")
                            .append("pCode", "$data.pCode")
                            .append("prc","$data.prc")
                            .append("rQty","$data.rQty")
                            .append("rRsn", "$data.rRsn")
                            .append("rcvTim","$data.rcvTim")
                            .append("tQty","$data.tQty")
                            .append("tTyp","$data.tTyp")
                            .append("trgPrc", "$data.trgPrc")
                    ).as("ordStsLst");

    public static final AggregationOperation ORDER_LOGS_RESPONSE_GROUP_OPERATION_V1 =
            group("data.oID")
                    .first("data.cancel").as("cancel") // not needed
                    .first("data.edit").as("edit")// not needed
                    .first("data.exit").as("exit")// not needed
                    .first("data.edit").as("edit")// not needed

                    .first("data.sym").as("sym") // not changing
                    .first("data.dpName").as("dpName") // not changing
                    .first("data.aTyp").as("asTyp")// not changing
                    .first("data.cName").as("cpName")// not changing
                    .first("data.dExpDt").as("dpExpDt")// not changing
                    .first("data.dInsTyp").as("dpInsTyp")// not changing
                    .first("data.dQty").as("dsQty")// not changing
                    .first("data.desc").as("desc")// not changing
                    .first("data.dur").as("dur")// not changing
                    .first("data.desc").as("desc")// not changing
                    .first("data.exc").as("exc") // not changing
                    .first("data.lsz").as("ltSz")// not changing
                    .first("data.oID").as("ordID")// not changing
                    .first("data.ogt").as("ogt")// not changing
                    .first("data.opTyp").as("opTyp")// not changing
                    .first("data.srs").as("srs")// not changing
                    .first("data.stkPrc").as("stkPrc")// not changing
                    .first("data.tSym").as("trdSym")// not changing
                    .first("data.tsz").as("tkSz")// not changing
                    .first("data.pTyp").as("pcktTyp") // not changing
                    .first("data.rcvDt").as("rcvDt") // not changing

                    .push(new BasicDBObject("isCOSecLeg", "$data.isCOSecLeg")
                                    .append("extOrdTim", "$data.extOrdTim")
                                    .append("sts", "$data.sts")
                                    .append("avgPrc", "$data.avgPrc")
                                    .append("exONo", "$data.exONo")
                                    .append("flQty", "$data.fQty")
                                    .append("nstReqID", "$data.nReqID")
                                    .append("ordTyp", "$data.oTyp")
                                    .append("prdCode", "$data.pCode")
                                    .append("prc", "$data.prc")
//                            .append("reqQty","$data.rQty")
                                    .append("pdQty", "$data.rQty")
                                    .append("rjRsn", "$data.rRsn")
                                    .append("rcvTim", "$data.rcvTim")
                                    .append("ntQty", "$data.tQty") //then what is nt quantity!
                                    .append("trsTyp", "$data.tTyp")
                                    .append("trgPrc", "$data.trgPrc")
                    ).as("ordStsLst");


    public static final AggregationOperation TRADE_BOOK_RESPONSE_GROUP_OPERATION =
            group("data.oID")
                    .first("data.pTyp").as("pTyp")
                    .first("data.aTyp").as("aTyp")
                    .first("data.cName").as("cName")
                    .first("data.chg").as("chg")
                    .first("data.chgP").as("chgP")
                    .first("data.dExpDt").as("dExpDt")
                    .first("data.dInsTyp").as("dInsTyp")
                    .first("data.dpName").as("dpName")
                    .first("data.exONo").as("exONo")
                    .first("data.exc").as("exc")
                    .first("data.lsz").as("lsz")
                    .first("data.ltp").as("ltp")
                    .first("data.oID").as("oID")
                    .first("data.oTyp").as("oTyp")
                    .first("data.opTyp").as("opTyp")
                    .first("data.pCode").as("pCode")
                    .first("data.pConv").as("pConv")
                    .first("data.qty").as("qty")
                    .first("data.rcvTim").as("rcvTim")
                    .first("data.rcvDt").as("rcvDt")
                    .first("data.srs").as("srs")
                    .first("data.stkPrc").as("stkPrc")
                    .first("data.sts").as("sts")
                    .first("data.sym").as("sym")
                    .first("data.tPrc").as("ntPrc") // this was done only for trade book
                    .first("data.tSym").as("trdSym")
                    .first("data.tTyp").as("trsTyp")
                    .first("data.tsz").as("tkSz")
                    .first("data.extOrdTim").as("extOrdTim")
                    .push(new BasicDBObject("flId","$data.fID")
                            .append("flPrc","$data.fPrc")
                            .append("flQty","$data.fQty")).as("flDtlLst");



    public static final AggregationOperation ORDER_BOOK_RESPONSE_GROUP_OPERATION =
            group("data.oID")
                    .first("data.sts").as("sts")
                    .first("data.sym").as("sym")
                    .first("data.dpName").as("dpName")
                    .first("data.extOrdTim").as("extOrdTim")
                    .first("data.aTyp").as("aTyp")
                    .first("data.avgPrc").as("avgPrc")
                    .first("data.cName").as("cName")
                    .first("data.cancel").as("cancel")
                    .first("data.dExpDt").as("dExpDt")
                    .first("data.dInsTyp").as("dInsTyp")
                    .first("data.dQty").as("dQty")
                    .first("data.desc").as("desc")
                    .first("data.dur").as("dur")
                    .first("data.desc").as("desc")
                    .first("data.exONo").as("exONo")
                    .first("data.edit").as("edit")
                    .first("data.exc").as("exc")
                    .first("data.exit").as("exit")
                    .first("data.fQty").as("fQty")
                    .first("data.edit").as("edit")
                    .first("data.lsz").as("lsz")
                    .first("data.nReqID").as("nReqID")
                    .first("data.oID").as("oID")
                    .first("data.oTyp").as("oTyp")
                    .first("data.ogt").as("ogt")
                    .first("data.opTyp").as("opTyp")
                    .first("data.pTyp").as("pTyp")
                    .first("data.pCode").as("pCode")
                    .first("data.prc").as("prc")
                    .first("data.rQty").as("rQty")
                    .first("data.rRsn").as("rRsn")
                    .first("data.srs").as("srs")
                    .first("data.stkPrc").as("stkPrc")
                    .first("data.rcvTim").as("rcvTim")
                    .first("data.rcvDt").as("rcvDt")
                    .first("data.sID").as("sID")
                    .first("data.tSym").as("tSym")
                    .first("data.tQty").as("tQty")
                    .first("data.tTyp").as("tTyp")
                    .first("data.trgPrc").as("trgPrc")
                    .first("data.tsz").as("tsz")
                    .first("data.isCOSecLeg").as("isCOSecLeg")
                    .first("accId").as("accId")
                    .first("data.syomId").as("syomID")
                    .first("data.usrCmnt").as("userCmnt");

    public static final AggregationOperation ORDER_BOOK_RESPONSE_GROUP_OPERATION_V1=
            group("data.oID")
                    .first("data.sts").as("sts")
                    .first("data.sym").as("sym")
                    .first("data.dpName").as("dpName")
                    .first("data.extOrdTim").as("extOrdTim")
                    .first("data.aTyp").as("asTyp")
                    .first("data.avgPrc").as("avgPrc")
                    .first("data.cName").as("cpName")
                    .first("data.cancel").as("cancel")
                    .first("data.dExpDt").as("dpExpDt")
                    .first("data.dInsTyp").as("dpInsTyp")
                    .first("data.dQty").as("dsQty")
                    .first("data.desc").as("desc")
                    .first("data.dur").as("dur")
                    .first("data.desc").as("desc")
                    .first("data.exONo").as("exONo")
                    .first("data.edit").as("edit")
                    .first("data.exc").as("exc")
                    .first("data.exit").as("exit")
                    .first("data.fQty").as("flQty")
                    .first("data.edit").as("edit")
                    .first("data.lsz").as("ltSz")
                    .first("data.nReqID").as("nstReqID")
                    .first("data.oID").as("ordID")
                    .first("data.oTyp").as("ordTyp")
                    .first("data.ogt").as("ogt")
                    .first("data.opTyp").as("opTyp")
                    .first("data.pTyp").as("pcktTyp")
                    .first("data.pCode").as("prdCode")
                    .first("data.prc").as("prc")
//                    .first("data.rQty").as("reqQty")
                    .first("data.rQty").as("pdQty")
                    .first("data.rRsn").as("rjRsn")
                    .first("data.rcvTim").as("rcvTim")
                    .first("data.extOrdTim").as("rcvEpTim")
                    .first("data.srs").as("srs")
                    .first("data.stkPrc").as("stkPrc")
                    .first("data.sID").as("sipID")
                    .first("data.tSym").as("trdSym")
//                    .first("data.tQty").as("pdQty")
                    .first("data.tQty").as("ntQty")
                    .first("data.tTyp").as("trsTyp")
                    .first("data.trgPrc").as("trgPrc")
                    .first("data.tsz").as("tkSz")
                    .first("data.isCOSecLeg").as("isCOSecLeg")
                    .first("accId").as("accId")
                    .first("data.rcvDt").as("rcvDt")
                    .first("data.vlDt").as("vlDt")
                    .first("data.syomId").as("syomID")
                    .first("data.usrCmnt").as("userCmnt")
                    .first("data.boSeqId").as("boSeqId")
                    .push(new BasicDBObject("flID","$data.fID")
                            .append("flPrc","$data.fPrc")
                            .append("flTim", "$data.rcvTim")
                            .append("flDt", "$data.rcvDt")
                            .append("flQty","$data.fQty")).as("flDtlLst");


    public static final AggregationOperation TRADE_BOOK_RESPONSE_GROUP_OPERATION_V1 =
            group("data.oID")
                    .first("data.pTyp").as("pcktTyp")
                    .first("data.aTyp").as("asTyp")
                    .first("data.cName").as("cpName")
                    .first("data.dExpDt").as("dpExpDt")
                    .first("data.dInsTyp").as("dpInsTyp")
                    .first("data.dpName").as("dpName")
                    .first("data.exONo").as("exONo")
                    .first("data.exc").as("exc")
                    .first("data.lsz").as("ltSz")
                    .first("data.ltp").as("ltp")
                    .first("data.oID").as("ordID")
                    .first("data.oTyp").as("ordTyp")
                    .first("data.opTyp").as("opTyp")
                    .first("data.pCode").as("prdCode")
                    .first("data.nReqID").as("nstReqID")
                    .first("data.pConv").as("psCnv")
                    .first("data.qty").as("qty")
                    .first("data.rcvTim").as("rcvTim")
                    .first("data.rcvDt").as("rcvDt")
                    .first("data.srs").as("srs")
                    .first("data.stkPrc").as("stkPrc")
                    .first("data.sts").as("sts")
                    .first("data.sym").as("sym")
//                    .first("data.tPrc").as("ntPrc")
                    .first("data.tSym").as("trdSym")
                    .first("data.tTyp").as("trsTyp")
                    .first("data.tsz").as("tkSz")
                    .first("data.extOrdTim").as("rcvEpTim")
                    .first("data.vlDt").as("vlDt")
                    .first("data.syomId").as("syomID")
                    .first("data.usrCmnt").as("userCmnt")
                    .first("data.fPrc").as("ntPrc") // this is the average
                    .first("data.fQty").as("qtyUnits")
                    .push(new BasicDBObject("flID","$data.fID")
                            .append("flPrc","$data.fPrc")
                            .append("flTim", "$data.rcvTim")
                            .append("flDt", "$data.rcvDt")
                            .append("flQty","$data.fQty")).as("flDtlLst");


}
