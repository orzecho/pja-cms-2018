/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NyanTestModule } from '../../../test.module';
import { LessonFileDetailComponent } from 'app/entities/lesson-file/lesson-file-detail.component';
import { LessonFile } from 'app/shared/model/lesson-file.model';

describe('Component Tests', () => {
    describe('LessonFile Management Detail Component', () => {
        let comp: LessonFileDetailComponent;
        let fixture: ComponentFixture<LessonFileDetailComponent>;
        const route = ({ data: of({ lessonFile: new LessonFile(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [LessonFileDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(LessonFileDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(LessonFileDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.lessonFile).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
