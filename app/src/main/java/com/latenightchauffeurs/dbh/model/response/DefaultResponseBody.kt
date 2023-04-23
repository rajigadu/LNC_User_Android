package com.latenightchauffeurs.dbh.model.response

data class DefaultResponseBody(
    val data: List<DefaultResponseBodyData>,
    val status: String
)