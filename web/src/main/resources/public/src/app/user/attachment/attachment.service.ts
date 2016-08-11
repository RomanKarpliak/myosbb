import {Injectable} from "@angular/core";
import {Headers, Http} from "@angular/http";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/map";
import {Attachment} from "./attachment.interface";
import "rxjs/add/operator/toPromise";
import ApiService = require("../../../shared/services/api.service");

@Injectable()
export class AttachmentService {

    private getAttachmentUrl = ApiService.serverUrl + '/restful/attachment?pageNumber=';
    private delAttachmentUrl = ApiService.serverUrl + '/restful/attachment/';
    private delAllAttachmentUrl = ApiService.serverUrl + '/restful/attachment/';
    private updateAttachmentUrl = ApiService.serverUrl + '/restful/attachment/';
    private downloadAttachmentUrl = ApiService.serverUrl + '/restful/attachment/';

    constructor(private _http:Http) {
    }

    getAllAttachments(pageNumber:number):Observable<any> {
        return this._http.get(this.getAttachmentUrl + pageNumber)
            .map((response)=> response.json())
            .catch((error)=>Observable.throw(error));
    }

    getAllAttachmentsSorted(pageNumber:number, name:string, order:boolean):Observable<any> {
        return this._http.get(this.getAttachmentUrl + pageNumber + '&&sortedBy=' + name + '&&asc=' + order)
            .map((response)=> response.json())
            .catch((error)=>Observable.throw(error));
    }

    deleteAttachmentById(attachmentId:number) {
        let headers = new Headers();
        headers.append('Content-Type', 'application/json');
        let url = this.delAttachmentUrl + attachmentId;
        console.log('delete attachment by id: ' + attachmentId);
        return this._http.delete(url, {headers: headers})
            .toPromise()
            .catch((error)=>console.error(error));

    }

    deleteAllAttachments() {
        console.log('delete all attachments');
        return this._http.delete(this.delAllAttachmentUrl)
            .toPromise()
            .catch((error)=>console.error(error));
    }

    editAndSave(attachment:Attachment) {
        if (attachment.attachmentId) {
            console.log('updating attachment with id: ' + attachment.attachmentId);
            this.put(attachment);
        }
    }

    put(attachment:Attachment) {
        let headers = new Headers();
        headers.append('Content-Type', 'application/json');
        return this._http.put(this.updateAttachmentUrl, JSON.stringify(attachment), {headers: headers})
            .toPromise()
            .then(()=>attachment)
            .catch((error)=>console.error(error));
    }

    uploadAttachment(attachment:Attachment): Promise<Attachment> {
        let headers = new Headers();
        headers.append('Content-Type', 'multipart/form-data');
        return this._http.post(this.downloadAttachmentUrl, attachment, {headers: headers})
            .toPromise()
            .then(()=>attachment)
            .catch((error)=>console.error(error));
    }
}