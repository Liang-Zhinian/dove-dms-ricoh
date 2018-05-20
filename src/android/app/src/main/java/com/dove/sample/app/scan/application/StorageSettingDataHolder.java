/*
 *  Copyright (C) 2014 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.app.scan.application;

/**
 * 蓄積ファイル設定クラスです。
 * Storage setting data class.
 */
public class StorageSettingDataHolder {

    /**
     * ローカル蓄積設定です。
     * Local storage setting data.
     */
    private StorageStoreData mStorageStoreData;

    /**
     * 蓄積ファイル送信設定です。
     * Stored file setting data.
     */
    private StorageSendData mStorageSendData;

    /**
     * ローカル蓄積設定クラスです。
     * Local storage setting data class.
     */
    public static class StorageStoreData {
        private String folderId;
        private String folderName;
        private String folderPass;
        private String fileName;
        private String filePass;
        public String getFolderId() {
            return folderId;
        }
        public void setFolderId(String folderId) {
            this.folderId = folderId;
        }
        public String getFolderName() {
            return folderName;
        }
        public void setFolderName(String folderName) {
            this.folderName = folderName;
        }
        public String getFolderPass() {
            return folderPass;
        }
        public void setFolderPass(String folderPass) {
            this.folderPass = folderPass;
        }
        public String getFileName() {
            return fileName;
        }
        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
        public String getFilePass() {
            return filePass;
        }
        public void setFilePass(String filePass) {
            this.filePass = filePass;
        }
    }

    /**
     * 蓄積ファイル送信設定クラスです。
     * Stored file setting.
     */
    public static class StorageSendData {
        private String folderId;
        private String folderName;
        private String folderPass;
        private String fileId;
        private String fileName;
        private String filePass;
        public String getFolderId() {
            return folderId;
        }
        public void setFolderId(String folderId) {
            this.folderId = folderId;
        }
        public String getFolderName() {
            return folderName;
        }
        public void setFolderName(String folderName) {
            this.folderName = folderName;
        }
        public String getFolderPass() {
            return folderPass;
        }
        public void setFolderPass(String folderPass) {
            this.folderPass = folderPass;
        }
        public String getFileId() {
            return fileId;
        }
        public void setFileId(String fileId) {
            this.fileId = fileId;
        }
        public String getFileName() {
            return fileName;
        }
        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
        public String getFilePass() {
            return filePass;
        }
        public void setFilePass(String filePass) {
            this.filePass = filePass;
        }
    }

    public StorageStoreData getStorageStoreData() {
        return mStorageStoreData;
    }

    public void setStorageStoreData(StorageStoreData storageStoreData) {
        this.mStorageStoreData = storageStoreData;
    }

    public StorageSendData getStorageSendData() {
        return mStorageSendData;
    }

    public void setStorageSendData(StorageSendData storageSendData) {
        this.mStorageSendData = storageSendData;
    }

}
