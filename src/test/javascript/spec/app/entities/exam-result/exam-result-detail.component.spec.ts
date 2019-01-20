/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NyanTestModule } from '../../../test.module';
import { ExamResultDetailComponent } from 'app/entities/exam-result/exam-result-detail.component';
import { ExamResult } from 'app/shared/model/exam-result.model';

describe('Component Tests', () => {
    describe('ExamResult Management Detail Component', () => {
        let comp: ExamResultDetailComponent;
        let fixture: ComponentFixture<ExamResultDetailComponent>;
        const route = ({ data: of({ examResult: new ExamResult(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [ExamResultDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ExamResultDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ExamResultDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.examResult).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
