'use strict';

import * as account from './account';
import * as document from './document';
import * as folder from './folder';

// account actions
export const saveAccount = account.saveAccount;
export const login = account.login;
export const logout = account.logout;
export const valid = account.valid;
export const renew = account.renew;
export const setNeedReloading = account.setNeedReloading;

// document actions
export const fetchingList = document.fetchingList;
export const doneFetchingList = document.doneFetchingList;
export const updateDocument = document.update;

export const chooseFolder = document.chooseFolder;
export const chooseDocument = document.chooseDocument;
export const upload = document.upload;
export const updateProgress = document.updateProgress;

export const toggleEditMode = document.toggleEditMode;

export const updateDownloadStatus = document.updateDownloadStatus;

// folder actions
export const updateFolder = folder.update;


