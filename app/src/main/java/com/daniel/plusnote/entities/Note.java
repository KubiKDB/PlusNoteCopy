package com.daniel.plusnote.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notes")
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "textNoteTitle")
    private String textNoteTitle;

    @ColumnInfo(name = "textNoteText")
    private String textNoteText;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "reminder")
    private boolean isReminderSet;

    @ColumnInfo(name = "reminder_id")
    private int reminder_id;

    @ColumnInfo(name = "reminder_activated")
    private boolean reminder_activated;

    @ColumnInfo(name = "rvi_checked")
    private boolean rvi_checked;

    @ColumnInfo(name = "list_item1")
    private String list_item1;
    @ColumnInfo(name = "list_item2")
    private String list_item2;
    @ColumnInfo(name = "list_item3")
    private String list_item3;
    @ColumnInfo(name = "list_item4")
    private String list_item4;
    @ColumnInfo(name = "list_item5")
    private String list_item5;
    @ColumnInfo(name = "list_item6")
    private String list_item6;
    @ColumnInfo(name = "list_item7")
    private String list_item7;
    @ColumnInfo(name = "list_item8")
    private String list_item8;
    @ColumnInfo(name = "list_item9")
    private String list_item9;
    @ColumnInfo(name = "list_item10")
    private String list_item10;
    @ColumnInfo(name = "list_item11")
    private String list_item11;
    @ColumnInfo(name = "list_item12")
    private String list_item12;
    @ColumnInfo(name = "list_item13")
    private String list_item13;
    @ColumnInfo(name = "list_item14")
    private String list_item14;
    @ColumnInfo(name = "list_item15")
    private String list_item15;
    @ColumnInfo(name = "list_item16")
    private String list_item16;
    @ColumnInfo(name = "list_item17")
    private String list_item17;
    @ColumnInfo(name = "list_item18")
    private String list_item18;
    @ColumnInfo(name = "list_item19")
    private String list_item19;
    @ColumnInfo(name = "list_item20")
    private String list_item20;
    @ColumnInfo(name = "list_item21")
    private String list_item21;
    @ColumnInfo(name = "list_item22")
    private String list_item22;
    @ColumnInfo(name = "list_item23")
    private String list_item23;
    @ColumnInfo(name = "list_item24")
    private String list_item24;
    @ColumnInfo(name = "list_item25")
    private String list_item25;

    @ColumnInfo(name = "is_checked1")
    private boolean is_checked1;
    @ColumnInfo(name = "is_checked2")
    private boolean is_checked2;
    @ColumnInfo(name = "is_checked3")
    private boolean is_checked3;
    @ColumnInfo(name = "is_checked4")
    private boolean is_checked4;
    @ColumnInfo(name = "is_checked5")
    private boolean is_checked5;
    @ColumnInfo(name = "is_checked6")
    private boolean is_checked6;
    @ColumnInfo(name = "is_checked7")
    private boolean is_checked7;
    @ColumnInfo(name = "is_checked8")
    private boolean is_checked8;
    @ColumnInfo(name = "is_checked9")
    private boolean is_checked9;
    @ColumnInfo(name = "is_checked10")
    private boolean is_checked10;
    @ColumnInfo(name = "is_checked11")
    private boolean is_checked11;
    @ColumnInfo(name = "is_checked12")
    private boolean is_checked12;
    @ColumnInfo(name = "is_checked13")
    private boolean is_checked13;
    @ColumnInfo(name = "is_checked14")
    private boolean is_checked14;
    @ColumnInfo(name = "is_checked15")
    private boolean is_checked15;
    @ColumnInfo(name = "is_checked16")
    private boolean is_checked16;
    @ColumnInfo(name = "is_checked17")
    private boolean is_checked17;
    @ColumnInfo(name = "is_checked18")
    private boolean is_checked18;
    @ColumnInfo(name = "is_checked19")
    private boolean is_checked19;
    @ColumnInfo(name = "is_checked20")
    private boolean is_checked20;
    @ColumnInfo(name = "is_checked21")
    private boolean is_checked21;
    @ColumnInfo(name = "is_checked22")
    private boolean is_checked22;
    @ColumnInfo(name = "is_checked23")
    private boolean is_checked23;
    @ColumnInfo(name = "is_checked24")
    private boolean is_checked24;
    @ColumnInfo(name = "is_checked25")
    private boolean is_checked25;


    @ColumnInfo(name = "is_list")
    private boolean is_list;
    @ColumnInfo(name = "is_image")
    private boolean is_image;
    @ColumnInfo(name = "is_video")
    private boolean is_video;
    @ColumnInfo(name = "is_voice")
    private boolean is_voice;
    @ColumnInfo(name = "from_gallery")
    private boolean from_gallery;
    @ColumnInfo(name = "is_photo")
    private boolean is_photo;


    @ColumnInfo(name = "image_path")
    private String image_path;
    @ColumnInfo(name = "photo_path")
    private String reminder_time;

    @ColumnInfo(name = "audio_length")
    private String audio_length;

    public boolean isRvi_checked() {
        return rvi_checked;
    }

    public void setRvi_checked(boolean rvi_checked) {
        this.rvi_checked = rvi_checked;
    }

    public int getReminder_id() {
        return reminder_id;
    }

    public void setReminder_id(int reminder_id) {
        this.reminder_id = reminder_id;
    }

    public boolean isReminderSet() {
        return isReminderSet;
    }

    public void setReminderSet(boolean reminderSet) {
        isReminderSet = reminderSet;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isIs_photo() {
        return is_photo;
    }

    public void setIs_photo(boolean is_photo) {
        this.is_photo = is_photo;
    }

    public String getAudio_length() {
        return audio_length;
    }

    public boolean isFrom_gallery() {
        return from_gallery;
    }

    public void setFrom_gallery(boolean from_gallery) {
        this.from_gallery = from_gallery;
    }

    public void setAudio_length(String audio_length) {
        this.audio_length = audio_length;
    }

    public boolean isIs_voice() {
        return is_voice;
    }

    public void setIs_voice(boolean is_voice) {
        this.is_voice = is_voice;
    }

    public boolean isIs_checked1() {
        return is_checked1;
    }

    public void setIs_checked1(boolean is_checked1) {
        this.is_checked1 = is_checked1;
    }

    public boolean isIs_checked2() {
        return is_checked2;
    }

    public void setIs_checked2(boolean is_checked2) {
        this.is_checked2 = is_checked2;
    }

    public boolean isIs_checked3() {
        return is_checked3;
    }

    public void setIs_checked3(boolean is_checked3) {
        this.is_checked3 = is_checked3;
    }

    public boolean isIs_checked4() {
        return is_checked4;
    }

    public void setIs_checked4(boolean is_checked4) {
        this.is_checked4 = is_checked4;
    }

    public boolean isIs_checked5() {
        return is_checked5;
    }

    public void setIs_checked5(boolean is_checked5) {
        this.is_checked5 = is_checked5;
    }

    public boolean isIs_checked6() {
        return is_checked6;
    }

    public void setIs_checked6(boolean is_checked6) {
        this.is_checked6 = is_checked6;
    }

    public boolean isIs_checked7() {
        return is_checked7;
    }

    public void setIs_checked7(boolean is_checked7) {
        this.is_checked7 = is_checked7;
    }

    public boolean isIs_checked8() {
        return is_checked8;
    }

    public void setIs_checked8(boolean is_checked8) {
        this.is_checked8 = is_checked8;
    }

    public boolean isIs_checked9() {
        return is_checked9;
    }

    public void setIs_checked9(boolean is_checked9) {
        this.is_checked9 = is_checked9;
    }

    public boolean isIs_checked10() {
        return is_checked10;
    }

    public void setIs_checked10(boolean is_checked10) {
        this.is_checked10 = is_checked10;
    }

    public boolean isIs_checked11() {
        return is_checked11;
    }

    public void setIs_checked11(boolean is_checked11) {
        this.is_checked11 = is_checked11;
    }

    public boolean isIs_checked12() {
        return is_checked12;
    }

    public void setIs_checked12(boolean is_checked12) {
        this.is_checked12 = is_checked12;
    }

    public boolean isIs_checked13() {
        return is_checked13;
    }

    public void setIs_checked13(boolean is_checked13) {
        this.is_checked13 = is_checked13;
    }

    public boolean isIs_checked14() {
        return is_checked14;
    }

    public void setIs_checked14(boolean is_checked14) {
        this.is_checked14 = is_checked14;
    }

    public boolean isIs_checked15() {
        return is_checked15;
    }

    public void setIs_checked15(boolean is_checked15) {
        this.is_checked15 = is_checked15;
    }

    public boolean isIs_checked16() {
        return is_checked16;
    }

    public void setIs_checked16(boolean is_checked16) {
        this.is_checked16 = is_checked16;
    }

    public boolean isIs_checked17() {
        return is_checked17;
    }

    public void setIs_checked17(boolean is_checked17) {
        this.is_checked17 = is_checked17;
    }

    public boolean isIs_checked18() {
        return is_checked18;
    }

    public void setIs_checked18(boolean is_checked18) {
        this.is_checked18 = is_checked18;
    }

    public boolean isIs_checked19() {
        return is_checked19;
    }

    public void setIs_checked19(boolean is_checked19) {
        this.is_checked19 = is_checked19;
    }

    public boolean isIs_checked20() {
        return is_checked20;
    }

    public void setIs_checked20(boolean is_checked20) {
        this.is_checked20 = is_checked20;
    }

    public boolean isIs_checked21() {
        return is_checked21;
    }

    public void setIs_checked21(boolean is_checked21) {
        this.is_checked21 = is_checked21;
    }

    public boolean isIs_checked22() {
        return is_checked22;
    }

    public void setIs_checked22(boolean is_checked22) {
        this.is_checked22 = is_checked22;
    }

    public boolean isIs_checked23() {
        return is_checked23;
    }

    public void setIs_checked23(boolean is_checked23) {
        this.is_checked23 = is_checked23;
    }

    public boolean isIs_checked24() {
        return is_checked24;
    }

    public void setIs_checked24(boolean is_checked24) {
        this.is_checked24 = is_checked24;
    }

    public boolean isIs_checked25() {
        return is_checked25;
    }

    public void setIs_checked25(boolean is_checked25) {
        this.is_checked25 = is_checked25;
    }

    public boolean isIs_image() {
        return is_image;
    }

    public String getReminder_time() {
        return reminder_time;
    }

    public boolean isIs_video() {
        return is_video;
    }

    public void setIs_video(boolean is_video) {
        this.is_video = is_video;
    }

    public void setReminder_time(String reminder_time) {
        this.reminder_time = reminder_time;
    }

    public void setIs_image(boolean is_image) {
        this.is_image = is_image;
    }

    public boolean isIs_list() {
        return is_list;
    }

    public void setIs_list(boolean is_list) {
        this.is_list = is_list;
    }

    public String getList_item1() {
        return list_item1;
    }

    public void setList_item1(String list_item1) {
        this.list_item1 = list_item1;
    }

    public String getList_item2() {
        return list_item2;
    }

    public void setList_item2(String list_item2) {
        this.list_item2 = list_item2;
    }

    public String getList_item3() {
        return list_item3;
    }

    public void setList_item3(String list_item3) {
        this.list_item3 = list_item3;
    }

    public String getList_item4() {
        return list_item4;
    }

    public void setList_item4(String list_item4) {
        this.list_item4 = list_item4;
    }

    public String getList_item5() {
        return list_item5;
    }

    public void setList_item5(String list_item5) {
        this.list_item5 = list_item5;
    }

    public String getList_item6() {
        return list_item6;
    }

    public void setList_item6(String list_item6) {
        this.list_item6 = list_item6;
    }

    public String getList_item7() {
        return list_item7;
    }

    public void setList_item7(String list_item7) {
        this.list_item7 = list_item7;
    }

    public String getList_item8() {
        return list_item8;
    }

    public void setList_item8(String list_item8) {
        this.list_item8 = list_item8;
    }

    public String getList_item9() {
        return list_item9;
    }

    public void setList_item9(String list_item9) {
        this.list_item9 = list_item9;
    }

    public String getList_item10() {
        return list_item10;
    }

    public void setList_item10(String list_item10) {
        this.list_item10 = list_item10;
    }

    public String getList_item11() {
        return list_item11;
    }

    public void setList_item11(String list_item11) {
        this.list_item11 = list_item11;
    }

    public String getList_item12() {
        return list_item12;
    }

    public void setList_item12(String list_item12) {
        this.list_item12 = list_item12;
    }

    public String getList_item13() {
        return list_item13;
    }

    public void setList_item13(String list_item13) {
        this.list_item13 = list_item13;
    }

    public String getList_item14() {
        return list_item14;
    }

    public void setList_item14(String list_item14) {
        this.list_item14 = list_item14;
    }

    public String getList_item15() {
        return list_item15;
    }

    public void setList_item15(String list_item15) {
        this.list_item15 = list_item15;
    }

    public String getList_item16() {
        return list_item16;
    }

    public void setList_item16(String list_item16) {
        this.list_item16 = list_item16;
    }

    public String getList_item17() {
        return list_item17;
    }

    public void setList_item17(String list_item17) {
        this.list_item17 = list_item17;
    }

    public String getList_item18() {
        return list_item18;
    }

    public void setList_item18(String list_item18) {
        this.list_item18 = list_item18;
    }

    public String getList_item19() {
        return list_item19;
    }

    public void setList_item19(String list_item19) {
        this.list_item19 = list_item19;
    }

    public String getList_item20() {
        return list_item20;
    }

    public void setList_item20(String list_item20) {
        this.list_item20 = list_item20;
    }

    public String getList_item21() {
        return list_item21;
    }

    public void setList_item21(String list_item21) {
        this.list_item21 = list_item21;
    }

    public String getList_item22() {
        return list_item22;
    }

    public void setList_item22(String list_item22) {
        this.list_item22 = list_item22;
    }

    public String getList_item23() {
        return list_item23;
    }

    public void setList_item23(String list_item23) {
        this.list_item23 = list_item23;
    }

    public String getList_item24() {
        return list_item24;
    }

    public void setList_item24(String list_item24) {
        this.list_item24 = list_item24;
    }

    public String getList_item25() {
        return list_item25;
    }

    public void setList_item25(String list_item25) {
        this.list_item25 = list_item25;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTextNoteTitle() {
        return textNoteTitle;
    }

    public void setTextNoteTitle(String textNoteTitle) {
        this.textNoteTitle = textNoteTitle;
    }

    public String getTextNoteText() {
        return textNoteText;
    }

    public void setTextNoteText(String textNoteText) {
        this.textNoteText = textNoteText;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    @NonNull
    @Override
    public String toString() {
        return "Note";
    }

    public boolean isReminder_activated() {
        return reminder_activated;
    }

    public void setReminder_activated(boolean reminder_activated) {
        this.reminder_activated = reminder_activated;
    }
}
