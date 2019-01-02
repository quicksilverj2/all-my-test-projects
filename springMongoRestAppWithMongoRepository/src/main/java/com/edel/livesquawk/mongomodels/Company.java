package com.edel.livesquawk.mongomodels;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Iqbal on 5/14/2018.
 */

class Company {
    @Getter
    @Setter
    private String __v;

    @Getter
    @Setter
    private String nameOfCompany;

    @Getter
    @Setter
    private String nse;

    @Getter
    @Setter
    private String bse;

    @Getter
    @Setter
    private String _id;
}
