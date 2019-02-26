import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IProposedWord } from 'app/shared/model/proposed-word.model';
import { ProposedWordService } from './proposed-word.service';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-proposed-word-update',
    templateUrl: './proposed-word-update.component.html'
})
export class ProposedWordUpdateComponent implements OnInit {
    private _proposedWord: IProposedWord;
    isSaving: boolean;

    tags: ITag[];

    users: IUser[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private proposedWordService: ProposedWordService,
        private tagService: TagService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ proposedWord }) => {
            this.proposedWord = proposedWord;
        });
        this.tagService.query().subscribe(
            (res: HttpResponse<ITag[]>) => {
                this.tags = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.proposedWord.id !== undefined) {
            this.subscribeToSaveResponse(this.proposedWordService.update(this.proposedWord));
        } else {
            this.subscribeToSaveResponse(this.proposedWordService.create(this.proposedWord));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IProposedWord>>) {
        result.subscribe((res: HttpResponse<IProposedWord>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackTagById(index: number, item: ITag) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get proposedWord() {
        return this._proposedWord;
    }

    set proposedWord(proposedWord: IProposedWord) {
        this._proposedWord = proposedWord;
    }
}
