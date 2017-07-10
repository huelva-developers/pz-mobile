package com.huelvadevelopers.proyectozero.model

import java.util.*

/**
 * Created by DrAP on 10/07/2017.
 */

class Transaction(val id : Int, val bankAccount: BankAccount, var category: Category, var description : String,
                  var date : Date, val amount : Int)