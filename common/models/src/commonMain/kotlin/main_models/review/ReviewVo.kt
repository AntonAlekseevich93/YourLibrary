package main_models.review

//todo remove this object and replace
class ReviewVo(
    val reviewText: String,
    val userName: String,
    val date: String, //todo replace with timemillis
    val rating: Int,
    val likeCount: Int,
    val dislikeCount: Int,
)