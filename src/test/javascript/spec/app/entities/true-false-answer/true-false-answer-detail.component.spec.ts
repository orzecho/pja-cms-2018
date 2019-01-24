/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NyanTestModule } from '../../../test.module';
import { TrueFalseAnswerDetailComponent } from 'app/entities/true-false-answer/true-false-answer-detail.component';
import { TrueFalseAnswer } from 'app/shared/model/true-false-answer.model';

describe('Component Tests', () => {
    describe('TrueFalseAnswer Management Detail Component', () => {
        let comp: TrueFalseAnswerDetailComponent;
        let fixture: ComponentFixture<TrueFalseAnswerDetailComponent>;
        const route = ({ data: of({ trueFalseAnswer: new TrueFalseAnswer(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [TrueFalseAnswerDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(TrueFalseAnswerDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TrueFalseAnswerDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.trueFalseAnswer).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
