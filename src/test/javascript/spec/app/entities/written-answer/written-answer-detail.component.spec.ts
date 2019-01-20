/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NyanTestModule } from '../../../test.module';
import { WrittenAnswerDetailComponent } from 'app/entities/written-answer/written-answer-detail.component';
import { WrittenAnswer } from 'app/shared/model/written-answer.model';

describe('Component Tests', () => {
    describe('WrittenAnswer Management Detail Component', () => {
        let comp: WrittenAnswerDetailComponent;
        let fixture: ComponentFixture<WrittenAnswerDetailComponent>;
        const route = ({ data: of({ writtenAnswer: new WrittenAnswer(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [WrittenAnswerDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(WrittenAnswerDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(WrittenAnswerDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.writtenAnswer).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
