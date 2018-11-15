import { Component, forwardRef, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ILesson } from 'app/shared/model/lesson.model';
import { LessonService } from './lesson.service';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag';
import { LessonFileService } from 'app/entities/lesson-file';
import { ILessonFile } from 'app/shared/model/lesson-file.model';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { Word } from 'app/shared/model/word.model';

@Component({
    selector: 'jhi-lesson-update',
    templateUrl: './lesson-update.component.html'
})
export class LessonUpdateComponent implements OnInit {
    private _lesson: ILesson;
    isSaving: boolean;

    tags: ITag[];
    availableFiles: ILessonFile[];

    displayDialog: boolean;
    word = new Word();
    selectedWord: Word;
    newWord: boolean;
    cols: [
        { field: 'translation'; header: 'Vin' },
        { field: 'kana'; header: 'Year' },
        { field: 'kanji'; header: 'Brand' },
        { field: 'romaji'; header: 'Color' },
        { field: 'note'; header: 'Color' },
        { field: 'tags'; header: 'Tagi' }
    ];

    constructor(
        private jhiAlertService: JhiAlertService,
        private lessonService: LessonService,
        private tagService: TagService,
        private activatedRoute: ActivatedRoute,
        private lessonFileService: LessonFileService
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ lesson }) => {
            this.lesson = lesson;
        });
        this.tagService.query().subscribe(
            (res: HttpResponse<ITag[]>) => {
                this.tags = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.lessonFileService.query({ 'lessonId.specified': false }).subscribe(
            (res: HttpResponse<ILessonFile[]>) => {
                this.availableFiles = res.body;
                if (this.lesson.lessonFiles) {
                    this.lesson.lessonFiles.forEach(file => this.availableFiles.push(file));
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.lesson.id !== undefined) {
            this.subscribeToSaveResponse(this.lessonService.update(this.lesson));
        } else {
            this.subscribeToSaveResponse(this.lessonService.create(this.lesson));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ILesson>>) {
        result.subscribe((res: HttpResponse<ILesson>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get lesson() {
        return this._lesson;
    }

    set lesson(lesson: ILesson) {
        this._lesson = lesson;
    }

    showDialogToAdd() {
        this.newWord = true;
        this.word = new Word();
        this.displayDialog = true;
    }

    saveWord() {
        if (!this.lesson.words) {
            this.lesson.words = [];
        }
        if (this.newWord) {
            this.lesson.words.push(this.word);
        } else {
            this.lesson.words[this.lesson.words.indexOf(this.selectedWord)] = this.word;
        }
        console.log(this.word);
        console.log(this.lesson.words);

        this.word = null;
        this.displayDialog = false;
    }

    deleteWord() {
        const index = this.lesson.words.indexOf(this.selectedWord);
        this.lesson.words = this.lesson.words.filter((val, i) => i !== index);
        this.word = null;
        this.displayDialog = false;
    }

    onRowSelect(event) {
        this.newWord = false;
        this.word = this.cloneWord(event.data);
        this.displayDialog = true;
    }

    cloneWord(word: Word): Word {
        return JSON.parse(JSON.stringify(word));
    }

    clearSelection() {
        this.selectedWord = null;
    }
}
