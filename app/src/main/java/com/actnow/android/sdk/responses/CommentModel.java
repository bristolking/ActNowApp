package com.actnow.android.sdk.responses;

public class CommentModel {
    private String comment;
    private String img;

    public CommentModel(String comment) {
        this.comment=comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public CommentModel(String comment, String img){
        this.comment= comment;
        this.img = img;

    }

    @Override
    public String toString() {
        return "CommentModel{" +
                "comment='" + comment + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
