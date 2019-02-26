import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IWord } from 'app/shared/model/word.model';
import { WordService } from './word.service';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag';
import { Principal } from 'app/core';

@Component({
    selector: 'jhi-word-update',
    templateUrl: './word-update.component.html'
})
export class WordUpdateComponent implements OnInit {
    private _word: IWord;
    isSaving: boolean;
    currentAccount;

    foundTags: string[];

    tags: ITag[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private wordService: WordService,
        private tagService: TagService,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ word }) => {
            this.word = word;
        });
        this.tagService.query().subscribe(
            (res: HttpResponse<ITag[]>) => {
                this.tags = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.word.id !== undefined) {
            this.subscribeToSaveResponse(this.wordService.update(this.word));
        } else {
            this.subscribeToSaveResponse(this.wordService.create(this.word));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IWord>>) {
        result.subscribe((res: HttpResponse<IWord>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get word() {
        return this._word;
    }

    set word(word: IWord) {
        this._word = word;
    }

    searchForTags(event) {
        this.tagService
            .findByNameContaining(event.query)
            .subscribe(
                (res: HttpResponse<ITag[]>) => (this.foundTags = res.body.map(tag => tag.name)),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    /**
     * custom implementation of adding new elements to the p-autocomplete
     * component, as currently it doesn't support this behavior by default
     */
    onTagInputKeyUp(event: KeyboardEvent) {
        console.log(event);
        if (event.key === 'Enter') {
            const tokenInput = event.srcElement as any;
            const inputValue = tokenInput.value;

            if (inputValue && !this.word.rawTags.includes(inputValue)) {
                this.word.rawTags.push(inputValue);
                tokenInput.value = '';
            }
        }
    }
}
