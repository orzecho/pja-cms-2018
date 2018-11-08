import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ILessonFile } from 'app/shared/model/lesson-file.model';

@Component({
    selector: 'jhi-lesson-file-detail',
    templateUrl: './lesson-file-detail.component.html'
})
export class LessonFileDetailComponent implements OnInit {
    lessonFile: ILessonFile;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ lessonFile }) => {
            this.lessonFile = lessonFile;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
