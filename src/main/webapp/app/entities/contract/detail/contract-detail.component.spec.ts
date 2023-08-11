import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ContractDetailComponent } from './contract-detail.component';

describe('Contract Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContractDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ContractDetailComponent,
              resolve: { contract: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(ContractDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load contract on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ContractDetailComponent);

      // THEN
      expect(instance.contract).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
