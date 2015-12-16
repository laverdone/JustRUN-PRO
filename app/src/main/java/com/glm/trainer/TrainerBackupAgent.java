package com.glm.trainer;

import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;

public class TrainerBackupAgent extends BackupAgentHelper {
	static final String USER_DB = "trainer";
	static final String DB_BACKUP_KEY="trainer_db";
	
	 public void onCreate() {
	        FileBackupHelper helper = new FileBackupHelper(this, USER_DB);
	        addHelper(DB_BACKUP_KEY, helper);
	 }
}
