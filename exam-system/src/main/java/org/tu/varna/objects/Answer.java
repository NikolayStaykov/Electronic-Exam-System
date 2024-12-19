package org.tu.varna.objects;

public class Answer {
    private Long Id;
    private String answerText;
    private Long questionId;
    private Double fraction;
    private Integer answerOrder;

    public Answer (Long id, String answerText, Long questionId, Double fraction, Integer answerOrder) {
        Id = id;
        this.answerText = answerText;
        this.questionId = questionId;
        this.fraction = fraction;
        this.answerOrder = answerOrder;
    }

    public Answer () {}

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Double getFraction() {
        return fraction;
    }

    public void setFraction(Double fraction) {
        this.fraction = fraction;
    }

    public Integer getAnswerOrder() {
        return answerOrder;
    }

    public void setAnswerOrder(Integer answerOrder) {
        this.answerOrder = answerOrder;
    }
}
